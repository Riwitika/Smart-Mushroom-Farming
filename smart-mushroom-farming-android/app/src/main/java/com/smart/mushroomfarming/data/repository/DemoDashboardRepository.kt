package com.smart.mushroomfarming.data.repository

import com.smart.mushroomfarming.domain.model.FarmingTelemetry
import com.smart.mushroomfarming.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoDashboardRepository @Inject constructor() : DashboardRepository {

    override fun getCurrentTelemetry(): Flow<FarmingTelemetry> = flow {
        emit(
            FarmingTelemetry(
                temperature = 24.5,
                humidity = 87.0,
                ventilation = "High",
                lightIntensity = "Medium",
                ph = 6.7,
                diseaseGrowthPossibility = "Low",
                confidence = 97,
                recommendation = "Environmental conditions are favourable. Continue maintaining current settings."
            )
        )
    }

    override fun getRecentPredictions(): Flow<List<FarmingTelemetry>> = flow {
        emit(
            listOf(
                FarmingTelemetry(
                    temperature = 24.5,
                    humidity = 87.0,
                    ventilation = "High",
                    lightIntensity = "Medium",
                    ph = 6.7,
                    diseaseGrowthPossibility = "Low",
                    confidence = 97,
                    recommendation = "Environmental conditions are favourable. Continue maintaining current settings.",
                    timestamp = System.currentTimeMillis() - 3600000 // 1 hour ago
                ),
                FarmingTelemetry(
                    temperature = 26.2,
                    humidity = 92.0,
                    ventilation = "Low",
                    lightIntensity = "High",
                    ph = 7.2,
                    diseaseGrowthPossibility = "Moderate",
                    confidence = 85,
                    recommendation = "High humidity and low ventilation detected. Consider increasing ventilation airflow.",
                    timestamp = System.currentTimeMillis() - 7200000 // 2 hours ago
                ),
                FarmingTelemetry(
                    temperature = 28.5,
                    humidity = 95.0,
                    ventilation = "Low",
                    lightIntensity = "High",
                    ph = 5.2,
                    diseaseGrowthPossibility = "High",
                    confidence = 91,
                    recommendation = "Critical conditions! High temp/humidity and low ventilation. Increase ventilation immediately and spray pH adjustment solution.",
                    timestamp = System.currentTimeMillis() - 14400000 // 4 hours ago
                )
            )
        )
    }

    override fun getFarmInsights(telemetry: FarmingTelemetry): List<String> {
        val insights = mutableListOf<String>()
        
        // Temperature check
        if (telemetry.temperature in 22.0..26.0) {
            insights.add("Temperature (${telemetry.temperature}°C) is suitable for healthy mushroom growth.")
        } else {
            insights.add("Warning: Temperature (${telemetry.temperature}°C) is outside the optimal range.")
        }

        // Humidity check
        if (telemetry.humidity in 80.0..90.0) {
            insights.add("Humidity (${telemetry.humidity}%) is within the optimal range.")
        } else {
            insights.add("Warning: Humidity (${telemetry.humidity}%) is outside the optimal range.")
        }

        // Ventilation check
        if (telemetry.ventilation == "High") {
            insights.add("Ventilation is adequate (High). Good Air Flow.")
        } else if (telemetry.ventilation == "Medium") {
            insights.add("Ventilation is moderate.")
        } else {
            insights.add("Warning: Ventilation is low. Stagnant air increases disease risks.")
        }

        // Light Intensity check
        if (telemetry.lightIntensity == "Medium" || telemetry.lightIntensity == "Low") {
            insights.add("Light intensity (${telemetry.lightIntensity}) is suitable for phototropic response.")
        } else {
            insights.add("Warning: Light intensity is high. Mushrooms prefer lower light.")
        }

        // pH check
        if (telemetry.ph in 6.0..7.0) {
            insights.add("pH (${telemetry.ph}) is stable and optimal.")
        } else {
            insights.add("Warning: pH (${telemetry.ph}) is unstable.")
        }

        return insights
    }
}
