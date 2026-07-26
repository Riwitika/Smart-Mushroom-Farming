package com.smart.mushroomfarming.data.network.dto

import com.google.gson.annotations.SerializedName

data class PredictionResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("health_status") val healthStatus: String,
    @SerializedName("disease_growth_possibility_level") val diseaseGrowthPossibilityLevel: String,
    @SerializedName("disease_risk_level") val diseaseRiskLevel: String,
    @SerializedName("recommendation") val recommendation: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("confidence") val confidence: Int? = null
)
