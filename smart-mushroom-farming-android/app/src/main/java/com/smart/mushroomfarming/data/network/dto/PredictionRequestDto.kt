package com.smart.mushroomfarming.data.network.dto

import com.google.gson.annotations.SerializedName

data class PredictionRequestDto(
    @SerializedName("temperature") val temperature: Double,
    @SerializedName("humidity") val humidity: Double,
    @SerializedName("ventilation") val ventilation: String,
    @SerializedName("light_intensity") val lightIntensity: String,
    @SerializedName("ph") val ph: Double
)
