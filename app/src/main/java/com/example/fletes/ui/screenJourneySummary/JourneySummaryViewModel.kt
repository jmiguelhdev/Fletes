package com.example.fletes.ui.screenJourneySummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fletes.data.room.JourneyWithBuyDetails // Corrected import as per specific task requirement
import com.example.fletes.data.repositories.interfaces.TruckJourneyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class JourneySummaryViewModel(
    private val repository: TruckJourneyRepository
) : ViewModel() {

    private val _journeys = MutableStateFlow<List<JourneyWithBuyDetails>>(emptyList())
    val journeys: StateFlow<List<JourneyWithBuyDetails>> = _journeys.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadJourneys()
    }

    private fun loadJourneys() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllJourneyWithBuyDetails() 
                .catch { e ->
                    _error.value = "Failed to load journeys: ${e.localizedMessage ?: "Unknown error"}"
                    _isLoading.value = false
                }
                .collect { data ->
                    _journeys.value = data
                    _isLoading.value = false
                }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
