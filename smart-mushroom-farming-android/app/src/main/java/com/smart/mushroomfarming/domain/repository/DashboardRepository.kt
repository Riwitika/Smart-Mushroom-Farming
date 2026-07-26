package com.smart.mushroomfarming.domain.repository

import com.smart.mushroomfarming.domain.model.FarmingTelemetry
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun getCurrentTelemetry(): Flow<FarmingTelemetry>
    fun getRecentPredictions(): Flow<List<FarmingTelemetry>>
    fun getFarmInsights(telemetry: FarmingTelemetry): List<String>
}
