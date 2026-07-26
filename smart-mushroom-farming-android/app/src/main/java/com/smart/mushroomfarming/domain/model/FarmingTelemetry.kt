package com.smart.mushroomfarming.domain.model

import java.util.UUID

data class FarmingTelemetry(
    val id: String = UUID.randomUUID().toString(),
    val temperature: Double,
    val humidity: Double,
    val ventilation: String, // "Low", "Medium", "High"
    val lightIntensity: String, // "Low", "Medium", "High"
    val ph: Double,
    val diseaseGrowthPossibility: String, // "Low", "Moderate", "High"
    val confidence: Int, // e.g. 97
    val recommendation: String,
    val timestamp: Long = System.currentTimeMillis()
)
