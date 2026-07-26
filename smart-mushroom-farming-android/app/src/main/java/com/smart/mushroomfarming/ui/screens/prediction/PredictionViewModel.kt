package com.smart.mushroomfarming.ui.screens.prediction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.mushroomfarming.domain.model.FarmingTelemetry
import com.smart.mushroomfarming.domain.repository.PredictionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PredictionUiState(
    val temperature: String = "",
    val temperatureError: String? = null,
    val humidity: String = "",
    val humidityError: String? = null,
    val ventilation: String = "Medium",
    val lightIntensity: String = "Medium",
    val ph: String = "",
    val phError: String? = null,
    val predictionResult: FarmingTelemetry? = null,
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false
)

@HiltViewModel
class PredictionViewModel @Inject constructor(
    private val repository: PredictionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PredictionUiState())
    val uiState: StateFlow<PredictionUiState> = _uiState.asStateFlow()

    fun onTemperatureChanged(value: String) {
        val temp = value.toDoubleOrNull()
        val error = when {
            value.isBlank() -> "Temperature is required"
            temp == null -> "Must be a valid number"
            temp < 0.0 || temp > 50.0 -> "Allow only realistic values (0-50°C)"
            else -> null
        }
        _uiState.value = _uiState.value.copy(
            temperature = value,
            temperatureError = error
        )
        validateForm()
    }

    fun onHumidityChanged(value: String) {
        val hum = value.toDoubleOrNull()
        val error = when {
            value.isBlank() -> "Humidity is required"
            hum == null -> "Must be a valid number"
            hum < 0.0 || hum > 100.0 -> "Humidity must be between 0 and 100%"
            else -> null
        }
        _uiState.value = _uiState.value.copy(
            humidity = value,
            humidityError = error
        )
        validateForm()
    }

    fun onVentilationChanged(value: String) {
        _uiState.value = _uiState.value.copy(ventilation = value)
        validateForm()
    }

    fun onLightIntensityChanged(value: String) {
        _uiState.value = _uiState.value.copy(lightIntensity = value)
        validateForm()
    }

    fun onPhChanged(value: String) {
        val phVal = value.toDoubleOrNull()
        val error = when {
            value.isBlank() -> "pH is required"
            phVal == null -> "Must be a valid decimal number"
            phVal < 0.0 || phVal > 14.0 -> "pH must be between 0 and 14"
            else -> null
        }
        _uiState.value = _uiState.value.copy(
            ph = value,
            phError = error
        )
        validateForm()
    }

    private fun validateForm() {
        val state = _uiState.value
        val isValid = state.temperature.isNotBlank() && state.temperatureError == null &&
                state.humidity.isNotBlank() && state.humidityError == null &&
                state.ph.isNotBlank() && state.phError == null
        _uiState.value = _uiState.value.copy(isFormValid = isValid)
    }

    fun runPrediction() {
        val state = _uiState.value
        if (!state.isFormValid) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, predictionResult = null)
            val temp = state.temperature.toDouble()
            val hum = state.humidity.toDouble()
            val phVal = state.ph.toDouble()
            
            repository.runPrediction(
                temperature = temp,
                humidity = hum,
                ventilation = state.ventilation,
                lightIntensity = state.lightIntensity,
                ph = phVal
            ).collect { result ->
                _uiState.value = _uiState.value.copy(
                    predictionResult = result,
                    isLoading = false
                )
            }
        }
    }

    fun clearResult() {
        _uiState.value = _uiState.value.copy(predictionResult = null)
    }
}
