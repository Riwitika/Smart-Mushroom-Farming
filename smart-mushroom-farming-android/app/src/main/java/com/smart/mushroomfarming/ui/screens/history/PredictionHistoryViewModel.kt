package com.smart.mushroomfarming.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.mushroomfarming.domain.model.FarmingTelemetry
import com.smart.mushroomfarming.domain.repository.PredictionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class FilterOption {
    NEWEST_FIRST,
    OLDEST_FIRST,
    HEALTHY_ONLY,
    MODERATE_ONLY,
    HIGH_RISK_ONLY
}

@HiltViewModel
class PredictionHistoryViewModel @Inject constructor(
    private val repository: PredictionRepository
) : ViewModel() {

    private val _history = MutableStateFlow<List<FarmingTelemetry>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterOption = MutableStateFlow(FilterOption.NEWEST_FIRST)
    val filterOption: StateFlow<FilterOption> = _filterOption.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _detailItem = MutableStateFlow<FarmingTelemetry?>(null)
    val detailItem: StateFlow<FarmingTelemetry?> = _detailItem.asStateFlow()

    val history: StateFlow<List<FarmingTelemetry>> = combine(
        _history,
        _searchQuery,
        _filterOption
    ) { list, search, filter ->
        var filteredList = list.filter { item ->
            search.isBlank() ||
            item.recommendation.contains(search, ignoreCase = true) ||
            item.temperature.toString().contains(search) ||
            item.ph.toString().contains(search) ||
            item.diseaseGrowthPossibility.contains(search, ignoreCase = true)
        }

        filteredList = when (filter) {
            FilterOption.NEWEST_FIRST -> filteredList.sortedByDescending { it.timestamp }
            FilterOption.OLDEST_FIRST -> filteredList.sortedBy { it.timestamp }
            FilterOption.HEALTHY_ONLY -> filteredList.filter { it.diseaseGrowthPossibility.lowercase() == "low" }
            FilterOption.MODERATE_ONLY -> filteredList.filter { it.diseaseGrowthPossibility.lowercase() == "moderate" }
            FilterOption.HIGH_RISK_ONLY -> filteredList.filter { it.diseaseGrowthPossibility.lowercase() == "high" }
        }

        filteredList
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                repository.getPredictionHistory().collect { list ->
                    _history.value = list
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load prediction history"
                _isLoading.value = false
            }
        }
    }

    fun refreshHistory() {
        loadHistory()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterOptionChanged(option: FilterOption) {
        _filterOption.value = option
    }

    fun clearError() {
        _errorMessage.value = null
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
