package com.smart.mushroomfarming.data.repository

import com.smart.mushroomfarming.data.network.api.PredictionApi
import com.smart.mushroomfarming.data.network.dto.PredictionRequestDto
import com.smart.mushroomfarming.domain.model.FarmingTelemetry
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

@Singleton
class RemotePredictionRepository @Inject constructor(
    private val api: PredictionApi
) : PredictionRepository {

    private val _history = MutableStateFlow<List<FarmingTelemetry>>(emptyList())

    init {
        // Pre-populate with initial history to match standard telemetry logs on app start
        val now = System.currentTimeMillis()
        _history.value = listOf(
            FarmingTelemetry(
                temperature = 24.5,
                humidity = 87.0,
                ventilation = "High",
                lightIntensity = "Medium",
                ph = 6.7,
                diseaseGrowthPossibility = "Low",
                confidence = 97,
                recommendation = "Continue current environmental conditions.",
                timestamp = now - 3600000
            ),
            FarmingTelemetry(
                temperature = 26.2,
                humidity = 92.0,
                ventilation = "Low",
                lightIntensity = "High",
                ph = 7.2,
                diseaseGrowthPossibility = "Moderate",
                confidence = 85,
                recommendation = "Increase ventilation and monitor humidity.",
                timestamp = now - 7200000
            ),
            FarmingTelemetry(
                temperature = 28.5,
                humidity = 95.0,
                ventilation = "Low",
                lightIntensity = "High",
                ph = 5.2,
                diseaseGrowthPossibility = "High",
                confidence = 91,
                recommendation = "Immediate inspection recommended.",
                timestamp = now - 14400000
            )
        )
    }

    override fun runPrediction(
        temperature: Double,
        humidity: Double,
        ventilation: String,
        lightIntensity: String,
        ph: Double
    ): Flow<FarmingTelemetry> = flow {
        try {
            // Execute network request DTO mappings
            val response = api.runPrediction(
                PredictionRequestDto(
                    temperature = temperature,
                    humidity = humidity,
                    ventilation = ventilation.lowercase(),
                    lightIntensity = lightIntensity.lowercase(),
                    ph = ph
                )
            )

            // Map DTO result back to telemetry domain model
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

            // Cache locally in-memory for history logs
            val currentList = _history.value.toMutableList()
            currentList.add(0, telemetry)
            _history.value = currentList

            emit(telemetry)
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
    }

    override fun getPredictionHistory(): Flow<List<FarmingTelemetry>> {
        return _history
    }

    override fun getPredictionById(id: String): Flow<FarmingTelemetry?> {
        return _history.map { list -> list.find { it.id == id } }
    }
}
