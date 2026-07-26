package com.smart.mushroomfarming.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Query
import com.smart.mushroomfarming.data.network.api.PredictionApi
import com.smart.mushroomfarming.data.network.dto.PredictionRequestDto
import com.smart.mushroomfarming.domain.model.FarmingTelemetry
import com.smart.mushroomfarming.domain.repository.AuthRepository
import com.smart.mushroomfarming.domain.repository.PredictionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

class FirestoreSyncException(
    val telemetry: FarmingTelemetry,
    message: String
) : Exception(message)

@Singleton
class RemotePredictionRepository @Inject constructor(
    private val api: PredictionApi,
    private val authRepository: AuthRepository
) : PredictionRepository {

    private val _history = MutableStateFlow<List<FarmingTelemetry>>(emptyList())
    private var lastUserId: String? = null

    override fun runPrediction(
        temperature: Double,
        humidity: Double,
        ventilation: String,
        lightIntensity: String,
        ph: Double
    ): Flow<FarmingTelemetry> = flow {
        // 1. Retrieve the authenticated user
        val user = authRepository.getCurrentUser() ?: throw Exception("User not authenticated.")

        // Ensure user ID matches the current cached history user ID
        if (user.uid != lastUserId) {
            _history.value = emptyList()
            lastUserId = user.uid
        }

        // 2. Query prediction output from FastAPI backend
        val response = try {
            api.runPrediction(
                PredictionRequestDto(
                    temperature = temperature,
                    humidity = humidity,
                    ventilation = ventilation.lowercase(),
                    lightIntensity = lightIntensity.lowercase(),
                    ph = ph
                )
            )
        } catch (e: UnknownHostException) {
            throw Exception("Prediction server unavailable. Please check your internet connection.")
        } catch (e: ConnectException) {
            throw Exception("Prediction server unavailable. Failed to connect to backend server.")
        } catch (e: SocketTimeoutException) {
            throw Exception("Connection Timeout. The server is taking too long to respond.")
        } catch (e: retrofit2.HttpException) {
            val errorMsg = when (e.code()) {
                422 -> "Invalid input parameters. Please check your inputs."
                500 -> "Internal Server Error in the prediction engine."
                else -> "HTTP Error ${e.code()}: ${e.message()}"
            }
            throw Exception(errorMsg)
        } catch (e: com.google.gson.JsonSyntaxException) {
            throw Exception("Serialization Error. Invalid response format from server.")
        } catch (e: Exception) {
            throw Exception(e.message ?: "An unexpected error occurred during prediction.")
        }

        // 3. Map result back to telemetry domain object
        val telemetry = FarmingTelemetry(
            temperature = temperature,
            humidity = humidity,
            ventilation = ventilation,
            lightIntensity = lightIntensity,
            ph = ph,
            diseaseGrowthPossibility = response.diseaseGrowthPossibilityLevel,
            confidence = response.confidence ?: 95,
            recommendation = response.recommendation,
            timestamp = System.currentTimeMillis()
        )

        // 4. Try saving the prediction to Cloud Firestore
        try {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(user.uid)

            // Save user profile info
            val userData = hashMapOf(
                "name" to (user.displayName ?: ""),
                "email" to user.email,
                "createdAt" to System.currentTimeMillis()
            )
            userRef.set(userData, SetOptions.merge()).await()

            // Save individual prediction
            val predictionRef = userRef.collection("predictions").document(telemetry.id)
            val predictionData = hashMapOf(
                "temperature" to telemetry.temperature,
                "humidity" to telemetry.humidity,
                "ventilation" to telemetry.ventilation,
                "lightIntensity" to telemetry.lightIntensity,
                "ph" to telemetry.ph,
                "healthStatus" to response.healthStatus,
                "diseaseGrowthPossibility" to telemetry.diseaseGrowthPossibility,
                "diseaseRisk" to response.diseaseRiskLevel,
                "recommendation" to telemetry.recommendation,
                "confidence" to telemetry.confidence,
                "timestamp" to telemetry.timestamp
            )
            predictionRef.set(predictionData).await()

            // Cache locally in memory
            val currentList = _history.value.toMutableList()
            currentList.add(0, telemetry)
            _history.value = currentList

            emit(telemetry)
        } catch (e: Exception) {
            // Local fallback caching to prevent losing prediction data
            val currentList = _history.value.toMutableList()
            currentList.add(0, telemetry)
            _history.value = currentList

            // Throw sync exception carrying the telemetry results
            throw FirestoreSyncException(telemetry, "Prediction saved locally but cloud sync failed.")
        }
    }

    override fun getPredictionHistory(): Flow<List<FarmingTelemetry>> {
        val user = authRepository.getCurrentUser()
        if (user == null) {
            _history.value = emptyList()
            lastUserId = null
            return _history
        }

        // Detect user account switches and clear log memory cache instantly
        if (user.uid != lastUserId) {
            _history.value = emptyList()
            lastUserId = user.uid
        }

        // Asynchronously load the prediction logs history from Firestore without blocking UI thread
        fetchHistoryFromFirestore(user.uid)

        return _history
    }

    private fun fetchHistoryFromFirestore(uid: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(uid)
            .collection("predictions")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { doc ->
                    FarmingTelemetry(
                        id = doc.id,
                        temperature = doc.getDouble("temperature") ?: 0.0,
                        humidity = doc.getDouble("humidity") ?: 0.0,
                        ventilation = doc.getString("ventilation") ?: "Medium",
                        lightIntensity = doc.getString("lightIntensity") ?: "Medium",
                        ph = doc.getDouble("ph") ?: 0.0,
                        diseaseGrowthPossibility = doc.getString("diseaseGrowthPossibility") ?: "Low",
                        confidence = doc.getLong("confidence")?.toInt() ?: 95,
                        recommendation = doc.getString("recommendation") ?: "",
                        timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                    )
                }
                _history.value = list
            }
    }

    override fun getPredictionById(id: String): Flow<FarmingTelemetry?> {
        return _history.map { list -> list.find { it.id == id } }
    }

    // Adapt GMS tasks to coroutines
    private suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T = suspendCancellableCoroutine { continuation ->
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(task.result)
            } else {
                continuation.resumeWithException(
                    task.exception ?: Exception("Unknown Firestore task failure")
                )
            }
        }
    }
}
