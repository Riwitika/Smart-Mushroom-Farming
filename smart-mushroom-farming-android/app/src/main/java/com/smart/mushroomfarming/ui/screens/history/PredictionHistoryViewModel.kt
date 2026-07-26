package com.smart.mushroomfarming.ui.screens.history

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

@HiltViewModel
class PredictionHistoryViewModel @Inject constructor(
    private val repository: PredictionRepository
) : ViewModel() {

    private val _history = MutableStateFlow<List<FarmingTelemetry>>(emptyList())
    val history: StateFlow<List<FarmingTelemetry>> = _history.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _detailItem = MutableStateFlow<FarmingTelemetry?>(null)
    val detailItem: StateFlow<FarmingTelemetry?> = _detailItem.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getPredictionHistory().collect { list ->
                    _history.value = list
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun refreshHistory() {
        loadHistory()
    }

    fun loadPredictionById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getPredictionById(id).collect { item ->
                    _detailItem.value = item
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }
}
