package com.smart.mushroomfarming.data.network.api

import com.smart.mushroomfarming.data.network.dto.PredictionRequestDto
import com.smart.mushroomfarming.data.network.dto.PredictionResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface PredictionApi {
    @POST("predict")
    suspend fun runPrediction(
        @Body request: PredictionRequestDto
    ): PredictionResponseDto
}
