package com.smart.mushroomfarming.domain.repository

import com.smart.mushroomfarming.domain.model.FarmingTelemetry
import kotlinx.coroutines.flow.Flow

interface PredictionRepository {
    fun runPrediction(
        temperature: Double,
        humidity: Double,
        ventilation: String,
        lightIntensity: String,
        ph: Double
    ): Flow<FarmingTelemetry>

    fun getPredictionHistory(): Flow<List<FarmingTelemetry>>
    fun getPredictionById(id: String): Flow<FarmingTelemetry?>
}
