package com.example.fletes.ui.screenBuyData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fletes.data.room.Buy
import com.example.fletes.data.room.JourneyWithBuyDetails
import com.example.fletes.domain.AddOrUpdateBuyDataUseCase
import com.example.fletes.domain.GetActiveJourneysForBuyScreenUseCase
import com.example.fletes.domain.GetBuyForJourneyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// BuyDataUiState can also be in BuyDataScreenState.kt, but for simplicity here:
data class BuyDataUiState(
    val journeys: List<JourneyWithBuyDetails> = emptyList(),
    val selectedJourney: JourneyWithBuyDetails? = null,
    val buyFormData: BuyFormData = BuyFormData(),
    val isLoadingJourneys: Boolean = false,
    val isLoadingBuyData: Boolean = false, // For loading existing Buy data
    val isSaving: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)

class BuyDataViewModel(
    private val addOrUpdateBuyDataUseCase: AddOrUpdateBuyDataUseCase,
    private val getBuyForJourneyUseCase: GetBuyForJourneyUseCase,
    private val getActiveJourneysForBuyScreenUseCase: GetActiveJourneysForBuyScreenUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BuyDataUiState())
    val uiState: StateFlow<BuyDataUiState> = _uiState.asStateFlow()

    init {
        loadActiveJourneys()
    }

    fun loadActiveJourneys() {
        _uiState.update { it.copy(isLoadingJourneys = true, error = null) }
        getActiveJourneysForBuyScreenUseCase()
            .onEach { loadedJourneys ->
                _uiState.update {
                    it.copy(
                        isLoadingJourneys = false,
                        journeys = loadedJourneys
                    )
                }
            }
            .catch { e ->
                _uiState.update {
                    it.copy(
                        isLoadingJourneys = false,
                        error = "Failed to load journeys: ${e.message}"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun selectJourney(journey: JourneyWithBuyDetails?) {
        if (journey == null) {
            _uiState.update { it.copy(selectedJourney = null, buyFormData = BuyFormData()) }
            return
        }

        _uiState.update { it.copy(selectedJourney = journey, isLoadingBuyData = true, buyFormData = BuyFormData()) }
        getBuyForJourneyUseCase(journey.journey.id)
            .onEach { existingBuy ->
                val formData = if (existingBuy != null) {
                    BuyFormData(
                        kg = existingBuy.kg.toString(),
                        price = existingBuy.price.toString(),
                        kgFaena = existingBuy.kgFaena.toString()
                        // Assuming Buy.isActive doesn't need to be in form; it's set false on save.
                    )
                } else {
                    BuyFormData()
                }
                _uiState.update {
                    it.copy(
                        isLoadingBuyData = false,
                        buyFormData = formData
                    )
                }
            }
            .catch { e ->
                _uiState.update {
                    it.copy(
                        isLoadingBuyData = false,
                        error = "Failed to load buy data: ${e.message}"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun validateDecimalInputInternal(input: String, fieldName: String, canBeZero: Boolean): String? {
        val normalizedInput = input.replace(',', '.')

        if (normalizedInput.isBlank()) {
            return "$fieldName cannot be empty"
        }

        val value = normalizedInput.toDoubleOrNull()
            ?: return "$fieldName must be a valid number"

        if (!canBeZero && value <= 0.0) {
            return "$fieldName must be positive"
        }
        if (canBeZero && value < 0.0) {
            return "$fieldName cannot be negative"
        }
        return null // No error
    }

    fun updateBuyKg(kg: String) {
        val error = validateDecimalInputInternal(kg, "Kg", canBeZero = false)
        _uiState.update {
            it.copy(buyFormData = it.buyFormData.copy(kg = kg, kgError = error ?: ""))
        }
    }

    fun updateBuyPrice(price: String) {
        val error = validateDecimalInputInternal(price, "Price", canBeZero = false)
        _uiState.update {
            it.copy(buyFormData = it.buyFormData.copy(price = price, priceError = error ?: ""))
        }
    }

    fun updateBuyKgFaena(kgFaena: String) {
        val error = validateDecimalInputInternal(kgFaena, "Kg Faena", canBeZero = true)
        _uiState.update {
            it.copy(buyFormData = it.buyFormData.copy(kgFaena = kgFaena, kgFaenaError = error ?: ""))
        }
    }
    
    private fun isFormDataValid(): Boolean {
        val formData = _uiState.value.buyFormData
        // Trigger updates to ensure error messages are current if fields were empty and not touched
        updateBuyKg(formData.kg)
        updateBuyPrice(formData.price)
        updateBuyKgFaena(formData.kgFaena)
        
        // Re-fetch the potentially updated form data
        val currentFormData = _uiState.value.buyFormData
        return currentFormData.kgError.isEmpty() &&
               currentFormData.priceError.isEmpty() &&
               currentFormData.kgFaenaError.isEmpty()
    }

    fun saveBuyData() {
        if (!isFormDataValid()) {
            // Error messages are already set by isFormDataValid() if fields were invalid
            return
        }

        val currentSelectedJourney = _uiState.value.selectedJourney
        if (currentSelectedJourney == null) {
            _uiState.update { it.copy(error = "No journey selected.") }
            return
        }

        _uiState.update { it.copy(isSaving = true, error = null) }

        val formData = _uiState.value.buyFormData
        val buyRecord = Buy(
            // id is auto-generated, if updating an existing one, its id would be from existingBuy.id
            // For this screen, we are creating a new Buy for an active journey.
            // If an existing 'buy' was loaded, we should use its id for update.
            // For simplicity now, assuming insert. The UseCase uses OnConflictStrategy.REPLACE.
            id = _uiState.value.selectedJourney?.buy?.id ?: 0, // Use existing ID if present (update), else 0 for new (insert)
            camionRegistroId = currentSelectedJourney.journey.id,
            kg = formData.kg.toDoubleOrNull() ?: 0.0,
            price = formData.price.toDoubleOrNull() ?: 0.0,
            kgFaena = formData.kgFaena.toDoubleOrNull() ?: 0.0,
            isActive = false // As per spec, Buy.isActive is false when data is loaded/complete
        )

        viewModelScope.launch {
            try {
                addOrUpdateBuyDataUseCase(buyRecord, currentSelectedJourney.journey)
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saveSuccess = true,
                        selectedJourney = null, // Clear selection
                        buyFormData = BuyFormData() // Reset form
                    )
                }
                loadActiveJourneys() // Refresh the list
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Failed to save buy data: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearSaveSuccessFlag() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
     fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
