package com.smart.mushroomfarming.data.repository

import com.smart.mushroomfarming.domain.model.FarmingTelemetry
import com.smart.mushroomfarming.domain.repository.PredictionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class InMemoryPredictionRepository @Inject constructor() : PredictionRepository {

    private val _history = MutableStateFlow<List<FarmingTelemetry>>(emptyList())

    init {
        // Pre-populate with initial logs to make the interface populated and professional
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
        delay(1500) // Simulated AI model processing latency
        
        // ML dataset variables threshold score rules
        val score = (if (temperature > 27.0) 2 else if (temperature < 20.0) 1 else 0) +
                (if (humidity > 90.0) 2 else if (humidity > 80.0) 1 else 0) +
                (if (ventilation == "Low") 2 else if (ventilation == "Medium") 1 else 0) +
                (if (ph < 5.5 || ph > 7.5) 2 else 0)

        val (possibility, confidence, recommendation) = when {
            score >= 6 -> Triple("High", Random.nextInt(85, 99), "Immediate inspection recommended.")
            score >= 3 -> Triple("Moderate", Random.nextInt(75, 95), "Increase ventilation and monitor humidity.")
            else -> Triple("Low", Random.nextInt(90, 99), "Continue current environmental conditions.")
        }

        val result = FarmingTelemetry(
            temperature = temperature,
            humidity = humidity,
            ventilation = ventilation,
            lightIntensity = lightIntensity,
            ph = ph,
            diseaseGrowthPossibility = possibility,
            confidence = confidence,
            recommendation = recommendation,
            timestamp = System.currentTimeMillis()
        )

        // Prepend to prediction logs list
        val currentList = _history.value.toMutableList()
        currentList.add(0, result)
        _history.value = currentList

        emit(result)
    }

    override fun getPredictionHistory(): Flow<List<FarmingTelemetry>> {
        return _history
    }

    override fun getPredictionById(id: String): Flow<FarmingTelemetry?> {
        return _history.map { list -> list.find { it.id == id } }
    }
}
