package com.smart.mushroomfarming.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.mushroomfarming.domain.model.FarmingTelemetry
import com.smart.mushroomfarming.domain.repository.DashboardRepository
import com.smart.mushroomfarming.domain.repository.PredictionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class DashboardUiState(
    val currentTelemetry: FarmingTelemetry? = null,
    val recentPredictions: List<FarmingTelemetry> = emptyList(),
    val insights: List<String> = emptyList(),
    val totalPredictions: Int = 0,
    val healthyPredictions: Int = 0,
    val moderatePredictions: Int = 0,
    val highRiskPredictions: Int = 0,
    val lastPredictionDate: String = "N/A",
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
    private val predictionRepository: PredictionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            combine(
                dashboardRepository.getCurrentTelemetry(),
                predictionRepository.getPredictionHistory()
            ) { telemetry, history ->
                val total = history.size
                val healthy = history.count { it.diseaseGrowthPossibility.lowercase() == "low" }
                val moderate = history.count { it.diseaseGrowthPossibility.lowercase() == "moderate" }
                val high = history.count { it.diseaseGrowthPossibility.lowercase() == "high" }

                val lastDate = if (history.isNotEmpty()) {
                    val sdf = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
                    sdf.format(Date(history.first().timestamp))
                } else {
                    "N/A"
                }

                DashboardUiState(
                    currentTelemetry = telemetry,
                    recentPredictions = history.take(10), // Take top 10 for charts and activity
                    insights = dashboardRepository.getFarmInsights(telemetry),
                    totalPredictions = total,
                    healthyPredictions = healthy,
                    moderatePredictions = moderate,
                    highRiskPredictions = high,
                    lastPredictionDate = lastDate,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}
