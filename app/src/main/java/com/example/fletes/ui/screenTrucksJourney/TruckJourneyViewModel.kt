package com.example.fletes.ui.screenTrucksJourney

import android.util.Log
import androidx.compose.animation.core.copy
import androidx.compose.foundation.layout.size
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fletes.data.model.DecimalTextFieldData
import com.example.fletes.data.model.truckJourneyData.TruckJourneyData
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.CamionesRegistro
import com.example.fletes.data.room.Destino
import com.example.fletes.data.room.JourneyWithAllDetails
import com.example.fletes.domain.GetActiveJourneysWithAllDetailsUseCase
import com.example.fletes.domain.GetAllJourneysWithAllDetailsUseCase
import com.example.fletes.domain.GetDestinationByIdUseCase
import com.example.fletes.domain.GetTruckByIdUseCase
import com.example.fletes.domain.GetTruckJourneyByIdUseCase
import com.example.fletes.domain.UpdateJourneyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class TruckJourneyUiState(
    val newJourneyFormData: TruckJourneyData = TruckJourneyData(
        camionId = 0,
        kmCargaData = DecimalTextFieldData("km carga", "", {}, ""),
        kmDescargaData = DecimalTextFieldData("km descarga", "", {}, ""),
        kmSurtidorData = DecimalTextFieldData("km surtidor", "", {}, ""),
        litrosData = DecimalTextFieldData("litros surtidos", "", {}, ""),
        isActive = false,
    ),
    val newJourneyTruckSelected: Camion = Camion(
        choferName = "",
        choferDni = 0,
        patenteTractor = "",
        patenteJaula = "",
        isActive = true
    ),
    val newJourneyDestinationSelected: Destino = Destino(
        comisionista = "",
        despacho = 0.0,
        localidad = "",
        isActive = true
    ),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val expandedJourneyId: Int? = null,
    val expandedJourneyDetails: CamionesRegistro? = null,
    val expandedJourneyTruck: Camion? = null,
    val expandedJourneyDestination: Destino? = null,
    val editableExpandedJourneyData: TruckJourneyData? = null,
    val checkedSwitch: Boolean = true,
    val journeysForDisplay: List<JourneyWithAllDetails> = emptyList()
)


class TruckJourneyViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getAllJourneysWithAllDetails: GetAllJourneysWithAllDetailsUseCase,
    private val getActiveJourneysWithAllDetails: GetActiveJourneysWithAllDetailsUseCase,
    private val getTruckByIdUseCase: GetTruckByIdUseCase,
    private val getDestinationByIdUseCase: GetDestinationByIdUseCase,
    private val getTruckJourneyByIdUseCase: GetTruckJourneyByIdUseCase,
    private val updateJourneyUseCase: UpdateJourneyUseCase
) : ViewModel() {

    // Claves para SavedStateHandle
    companion object {
        const val TRUCK_SELECTED_ID = "truckSelectedId"
        const val KM_CARGA_VALUE = "kmCargaValue"
        const val KM_DESCARGA_VALUE = "kmDescargaValue"
        const val KM_SURTIDOR_VALUE = "kmSurtidorValue"
        const val LITROS_VALUE = "litrosValue"
        const val IS_ACTIVE_VALUE = "isLastValue"
        const val KM_CARGA_ERROR = "kmCargaError"
        const val KM_DESCARGA_ERROR = "kmDescargaError"
        const val KM_SURTIDOR_ERROR = "kmSurtidorError"
        const val LITROS_ERROR = "litrosError"

    }


    // Inicializar el estado con los valores de SavedStateHandle o valores predeterminados
    private val _truckJourneyUiState = MutableStateFlow(
        TruckJourneyUiState(
            newJourneyFormData = TruckJourneyData(
                camionId = savedStateHandle[TRUCK_SELECTED_ID] ?: 0,
                kmCargaData = DecimalTextFieldData(
                    label = "km carga",
                    value = savedStateHandle[KM_CARGA_VALUE] ?: "",
                    onValueChange = { newValue ->
                        updateKmCargaValue(newValue)
                    },
                    errorMessage = savedStateHandle[KM_CARGA_ERROR] ?: ""
                ),
                kmDescargaData = DecimalTextFieldData(
                    label = "km descarga",
                    value = savedStateHandle[KM_DESCARGA_VALUE] ?: "",
                    onValueChange = { newValue ->
                        updateKmDescargaValue(newValue)
                    },
                    errorMessage = savedStateHandle[KM_DESCARGA_ERROR] ?: ""
                ),
                kmSurtidorData = DecimalTextFieldData(
                    label = "km surtidor",
                    value = savedStateHandle[KM_SURTIDOR_VALUE] ?: "",
                    onValueChange = { newValue ->
                        updateKmSurtidorValue(newValue)
                    },
                    errorMessage = savedStateHandle[KM_SURTIDOR_ERROR] ?: ""
                ),
                litrosData = DecimalTextFieldData(
                    label = "litros surtidos",
                    value = savedStateHandle[LITROS_VALUE] ?: "",
                    onValueChange = { newValue ->
                        updateLitrosValue(newValue)
                    },
                    errorMessage = savedStateHandle[LITROS_ERROR] ?: ""
                ),
                isActive = savedStateHandle[IS_ACTIVE_VALUE] ?: false,
            )
        )
    )

    // Expone el estado como un StateFlow inmutable
    val truckJourneyUiState: StateFlow<TruckJourneyUiState> = _truckJourneyUiState.asStateFlow()


    fun onTruckSelected(camion: Camion) {
        savedStateHandle[TRUCK_SELECTED_ID] = camion.id
        _truckJourneyUiState.update { currentState ->
            currentState.copy(
                newJourneyFormData = currentState.newJourneyFormData.copy(camionId = camion.id)
            )
        }
        viewModelScope.launch {
            updateTruckIsActive(camion)
        }
    }

    private fun validateDecimalInput(value: String): Pair<String, String> {
        return try {
            value.toDouble()
            Pair(value, "") // Valid number, no error
        } catch (e: NumberFormatException) {
            Pair(value, "Debe ser un número") // Invalid number, return error message
        }
    }

    // Funciones para actualizar cada valor y guardar en SavedStateHandle
    private fun updateKmCargaValue(newValue: String) {
        val (value, error) = validateDecimalInput(newValue)
        savedStateHandle[KM_CARGA_VALUE] = value
        savedStateHandle[KM_CARGA_ERROR] = error
        _truckJourneyUiState.update {
            it.copy(
                newJourneyFormData = it.newJourneyFormData.copy(
                    kmCargaData = it.newJourneyFormData.kmCargaData.copy(
                        value = value,
                        errorMessage = error
                    )
                )
            )
        }
    }

    private fun updateKmDescargaValue(newValue: String) {
        val (value, error) = validateDecimalInput(newValue)
        savedStateHandle[KM_DESCARGA_VALUE] = value
        savedStateHandle[KM_DESCARGA_ERROR] = error
        _truckJourneyUiState.update {
            it.copy(
                newJourneyFormData = it.newJourneyFormData.copy(
                    kmDescargaData = it.newJourneyFormData.kmDescargaData.copy(
                        value = value,
                        errorMessage = error
                    )
                )
            )
        }
    }

    private fun updateKmSurtidorValue(newValue: String) {
        val (value, error) = validateDecimalInput(newValue)
        savedStateHandle[KM_SURTIDOR_VALUE] = value
        savedStateHandle[KM_SURTIDOR_ERROR] = error
        _truckJourneyUiState.update {
            it.copy(
                newJourneyFormData = it.newJourneyFormData.copy(
                    kmSurtidorData = it.newJourneyFormData.kmSurtidorData.copy(
                        value = value,
                        errorMessage = error
                    )
                )
            )
        }
    }

    private fun updateLitrosValue(newValue: String) {
        val (value, error) = validateDecimalInput(newValue)
        savedStateHandle[LITROS_VALUE] = value
        savedStateHandle[LITROS_ERROR] = error
        _truckJourneyUiState.update {
            it.copy(
                newJourneyFormData = it.newJourneyFormData.copy(
                    litrosData = it.newJourneyFormData.litrosData.copy(
                        value = value,
                        errorMessage = error
                    )
                )
            )
        }
    }

    fun updateIsActiveValue(newValue: Boolean) {
        savedStateHandle[IS_ACTIVE_VALUE] = newValue
        _truckJourneyUiState.update {
            it.copy(newJourneyFormData = it.newJourneyFormData.copy(isActive = newValue))
        }
    }

    fun updateTruckIsActive(truck: Camion) {
        viewModelScope.launch {
            Log.d("TruckJourneyVM", "updateTruckIsActive: $truck")
            _truckJourneyUiState.update { currentState ->
                currentState.copy(
                    newJourneyTruckSelected = truck.copy(
                        isActive = false
                    ),
                )
            }

        }
    }

    //distancia debe ser unico para cada registro
// This function will be called by onCheckedChange and init
    fun observeJourneys() {
        viewModelScope.launch {
            _truckJourneyUiState.update { it.copy(isLoading = true, isError = false) }
            val showActive = _truckJourneyUiState.value.checkedSwitch
            Log.d("TruckJourneyVM", "observeJourneys called. showActive: $showActive")

            val journeysFlow = if (showActive) {
                getActiveJourneysWithAllDetails()
            } else {
                getAllJourneysWithAllDetails()
            }

            try {
                journeysFlow
                    .map { journeysList ->
                        journeysList.map { journeyWithDetails ->
                            val individualDistance = journeyWithDetails.journey.getDistancia()
                            val individualRate = journeyWithDetails.journey.getRateKmTimesLiters() // New calculation
                            journeyWithDetails.copy(
                                calculatedDistance = individualDistance,
                                calculatedRateKmLiters = individualRate // Include new rate
                            )
                        }
                    }
                    .onStart { Log.d("TruckJourneyVM", "Starting to load journeys based on switch: $showActive") }
                    .onCompletion { throwable ->
                        if (throwable == null) {
                            Log.d("TruckJourneyVM", "Journey loading completed for showActive: $showActive")
                        } else if (throwable is kotlinx.coroutines.CancellationException) {
                            Log.w("TruckJourneyVM", "Journey loading cancelled for showActive: $showActive", throwable)
                        }
                        else {
                            Log.e("TruckJourneyVM", "Journey loading failed for showActive: $showActive", throwable)
                        }
                    }
                    .collect { journeysWithDistance ->
                        Log.d("TruckJourneyVM", "Journeys with distance for showActive ($showActive): ${journeysWithDistance.size}")
                        _truckJourneyUiState.update {
                            it.copy(
                                isLoading = false,
                                journeysForDisplay = journeysWithDistance
                            )
                        }
                    }
            } catch (e: Exception) {
                Log.e("TruckJourneyVM", "Error in observeJourneys's try-catch block for showActive: $showActive", e)
                _truckJourneyUiState.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }

    init {
        observeJourneys()
    }

    fun onSwitchToggled(isChecked: Boolean) {
        _truckJourneyUiState.update {
            it.copy(
                checkedSwitch = isChecked
            )
        }
        observeJourneys()
    }

    // In TruckJourneyViewModel
    fun onClickJourneyCard(journeyId: Int) {
        Log.d(
            "TruckJourneyVM",
            "onClickJourneyCard called with ID: $journeyId. Current expandedID: ${_truckJourneyUiState.value.expandedJourneyId}"
        )
        viewModelScope.launch {
            val currentUiState = _truckJourneyUiState.value
            if (currentUiState.expandedJourneyId == journeyId) {
                Log.d("TruckJourneyVM", "Collapsing journey ID: $journeyId")
                _truckJourneyUiState.update {
                    it.copy(
                        expandedJourneyId = null,
                        expandedJourneyDetails = null,
                        expandedJourneyTruck = null,
                        expandedJourneyDestination = null,
                        editableExpandedJourneyData = null // Clear editable data
                    )
                }
            } else {
                Log.d("TruckJourneyVM", "Expanding journey ID: $journeyId. Fetching details...")
                _truckJourneyUiState.update {
                    it.copy(
                        isLoading = true,
                        expandedJourneyId = journeyId,
                        expandedJourneyDetails = null, // Will be set after fetch
                        expandedJourneyTruck = null,  // Will be set after fetch
                        expandedJourneyDestination = null, // Will be set after fetch
                        editableExpandedJourneyData = null  // Clear previous before fetching new
                    )
                }
                try {
                    val journeyDetails = getTruckJourneyByIdUseCase(journeyId).firstOrNull()
                    if (journeyDetails != null) {
                        val camion = getTruckByIdUseCase(journeyDetails.camionId)
                        val destino =
                            getDestinationByIdUseCase(journeyDetails.destinoId).firstOrNull()

                        // Create TruckJourneyData from the fetched journeyDetails
                        val editableData = TruckJourneyData(
                            camionId = journeyDetails.camionId, // Or whatever ID is appropriate
                            // Assuming your CamionesRegistro has these fields directly
                            // Adapt if they are nested or have different names
                            kmCargaData = DecimalTextFieldData(
                                label = "km carga",
                                value = journeyDetails.kmCarga.toString(), // Convert to String
                                onValueChange = { newValue -> updateExpandedKmCargaValue(newValue) },
                                errorMessage = "" // Initial error message
                            ),
                            kmDescargaData = DecimalTextFieldData(
                                label = "km descarga",
                                value = journeyDetails.kmDescarga.toString(),
                                onValueChange = { newValue -> updateExpandedKmDescargaValue(newValue) },
                                errorMessage = ""
                            ),
                            kmSurtidorData = DecimalTextFieldData(
                                label = "km surtidor",
                                value = journeyDetails.kmSurtidor.toString(),
                                onValueChange = { newValue -> updateExpandedKmSurtidorValue(newValue) },
                                errorMessage = ""
                            ),
                            litrosData = DecimalTextFieldData(
                                label = "litros surtidos",
                                value = journeyDetails.litros.toString(),
                                onValueChange = { newValue -> updateExpandedLitrosValue(newValue) },
                                errorMessage = ""
                            ),
                            isActive = journeyDetails.isActive // Assuming CamionesRegistro has isActive
                        )

                        _truckJourneyUiState.update {
                            it.copy(
                                isLoading = false,
                                expandedJourneyDetails = journeyDetails,
                                expandedJourneyTruck = camion,
                                expandedJourneyDestination = destino,
                                editableExpandedJourneyData = editableData // Set the editable data
                            )
                        }
                        Log.d(
                            "TruckJourneyVM",
                            "Updated state. New expandedID: ${_truckJourneyUiState.value.expandedJourneyId}, Details Loaded, EditableData Populated"
                        )
                    } else {
                        _truckJourneyUiState.update {
                            it.copy(
                                isLoading = false,
                                isError = true,
                                expandedJourneyId = null,
                                editableExpandedJourneyData = null
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e("TruckJourneyVM", "Error fetching details for $journeyId", e)
                    _truckJourneyUiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            expandedJourneyId = null,
                            editableExpandedJourneyData = null
                        )
                    }
                }
            }
        }
    }

    // In TruckJourneyViewModel

    fun updateExpandedIsActiveValue(newValue: Boolean) { // Removed 'id' as it's implicit
        _truckJourneyUiState.value.editableExpandedJourneyData?.let { currentEditableData ->
            // Only update if the new value is different, to avoid unnecessary recompositions (optional optimization)
            if (currentEditableData.isActive != newValue) {
                _truckJourneyUiState.update {
                    it.copy(
                        editableExpandedJourneyData = currentEditableData.copy(
                            isActive = newValue
                        )
                    )
                }
                saveExpandedJourneyDetails()
                // Optional: If you want to immediately persist this change to the database
                // you might call saveExpandedJourneyDetails() here, or have a separate
                // mechanism for auto-saving or explicit save.
                // For now, this just updates the UI state.
                Log.d("TruckJourneyVM", "Expanded isActive updated in UI state to: $newValue")
            }
        }
    }

    // For EXPANDED JOURNEY
    fun updateExpandedKmCargaValue(newValue: String) {
        _truckJourneyUiState.value.editableExpandedJourneyData?.let { currentEditableData ->
            val (value, error) = validateDecimalInput(newValue)
            _truckJourneyUiState.update { currentState ->
                val updatedEditableData = currentEditableData.copy(
                    kmCargaData = currentEditableData.kmCargaData.copy(
                        value = value,
                        errorMessage = error
                    )
                )
                val newIsActiveState = !isEditableExpandedJourneyComplete(updatedEditableData)
                currentState.copy(
                    editableExpandedJourneyData = updatedEditableData.copy(
                        isActive = newIsActiveState
                    )
                )
            }
        }
    }

    fun updateExpandedKmDescargaValue(newValue: String) {
        _truckJourneyUiState.value.editableExpandedJourneyData?.let { currentEditableData ->
            val (value, error) = validateDecimalInput(newValue)
            _truckJourneyUiState.update { currentState ->
                val updatedEditableData = currentEditableData.copy(
                    kmDescargaData = currentEditableData.kmDescargaData.copy(
                        value = value,
                        errorMessage = error
                    )
                )
                val newIsActiveState = !isEditableExpandedJourneyComplete(updatedEditableData)
                currentState.copy(
                    editableExpandedJourneyData = updatedEditableData.copy(
                        isActive = newIsActiveState
                    )
                )
            }
        }
    }

    fun updateExpandedKmSurtidorValue(newValue: String) {
        _truckJourneyUiState.value.editableExpandedJourneyData?.let { currentEditableData ->
            val (value, error) = validateDecimalInput(newValue)
            _truckJourneyUiState.update { currentState ->
                val updatedEditableData = currentEditableData.copy(
                    kmSurtidorData = currentEditableData.kmSurtidorData.copy(
                        value = value,
                        errorMessage = error
                    )
                )
                val newIsActiveState = !isEditableExpandedJourneyComplete(updatedEditableData)
                currentState.copy(
                    editableExpandedJourneyData = updatedEditableData.copy(
                        isActive = newIsActiveState
                    )
                )
            }
        }
    }

    fun updateExpandedLitrosValue(newValue: String) {
        _truckJourneyUiState.value.editableExpandedJourneyData?.let { currentEditableData ->
            val (value, error) = validateDecimalInput(newValue)
            _truckJourneyUiState.update { currentState ->
                val updatedEditableData = currentEditableData.copy(
                    litrosData = currentEditableData.litrosData.copy(
                        value = value,
                        errorMessage = error
                    )
                )
                val newIsActiveState = !isEditableExpandedJourneyComplete(updatedEditableData)
                currentState.copy(
                    editableExpandedJourneyData = updatedEditableData.copy(
                        isActive = newIsActiveState
                    )
                )
            }
        }
    }

    // Inside TruckJourneyViewModel
    private fun isEditableExpandedJourneyComplete(editableData: TruckJourneyData?): Boolean {
        editableData ?: return false // If there's no editable data, it's not complete

        return editableData.camionId != 0 && // Or other relevant ID check
                editableData.kmCargaData.value.isNotBlank() &&
                editableData.kmDescargaData.value.isNotBlank() &&
                editableData.kmSurtidorData.value.isNotBlank() &&
                editableData.litrosData.value.isNotBlank() &&
                // Ensure no validation errors
                editableData.kmCargaData.errorMessage.isEmpty() &&
                editableData.kmDescargaData.errorMessage.isEmpty() &&
                editableData.kmSurtidorData.errorMessage.isEmpty() &&
                editableData.litrosData.errorMessage.isEmpty()
    }


    // In TruckJourneyViewModel
    fun saveExpandedJourneyDetails() {
        val currentExpandedData = _truckJourneyUiState.value.editableExpandedJourneyData
        val originalDetails = _truckJourneyUiState.value.expandedJourneyDetails
        Log.d("TruckJourneyVM", "current id: ${_truckJourneyUiState.value.expandedJourneyId}")
        Log.d(
            "TruckJourneyVM",
            "originalDetails current id: ${_truckJourneyUiState.value.expandedJourneyDetails!!.id}"
        )

        if (currentExpandedData != null && originalDetails != null) {
            // 1. Validate currentExpandedData (check errorMessages)
            val hasErrors = listOf(
                currentExpandedData.kmCargaData.errorMessage,
                currentExpandedData.kmDescargaData.errorMessage,
                currentExpandedData.kmSurtidorData.errorMessage,
                currentExpandedData.litrosData.errorMessage
            ).any { it.isNotEmpty() }

            if (hasErrors) {
                Log.w(
                    "TruckJourneyVM",
                    "Cannot save, validation errors exist in expanded journey data."
                )
                // Optionally update UI state to show a general error message
                // _truckJourneyUiState.update { it.copy(isError = true, /* some error message */) }
                return
            }

            // 2. Convert TruckJourneyData back to a CamionesRegistro (or your update DTO)
            // This assumes you can update CamionesRegistro directly or have an update DTO
            // You might need to parse string values back to Double/Int
            try {
                val updatedJourney =
                    originalDetails.copy( // Create a new instance based on original
                        kmCarga = currentExpandedData.kmCargaData.value.toDoubleOrNull()?.toInt()
                            ?: originalDetails.kmCarga,
                        kmDescarga = currentExpandedData.kmDescargaData.value.toDoubleOrNull()
                            ?.toInt() ?: originalDetails.kmDescarga,
                        kmSurtidor = currentExpandedData.kmSurtidorData.value.toDoubleOrNull()
                            ?.toInt() ?: originalDetails.kmSurtidor,
                        litros = currentExpandedData.litrosData.value.toDoubleOrNull()
                            ?: originalDetails.litros,
                        isActive = currentExpandedData.isActive
                        // ... any other fields from TruckJourneyData
                    )

                viewModelScope.launch {
                    // 3. Call your update use case
                    updateJourneyUseCase(updatedJourney)
                    // e.g., updateJourneyUseCase(updatedJourney)
                    Log.d("TruckJourneyVM", "Attempting to save updated journey: $updatedJourney")
                    // After successful save, you might want to:
                    // - Refresh the list or update the item in _allJourneys
                    // - Potentially collapse the card or show a success message
                    // - Update expandedJourneyDetails with the saved version
                    _truckJourneyUiState.update {
                        it.copy(
                            expandedJourneyDetails = updatedJourney, // Reflect saved changes
                            editableExpandedJourneyData = null, // Clear editable form
                            expandedJourneyId = null // Collapse card after save (optional)
                        )
                    }
                    observeJourneys() // Refresh the list to show updated data based on the current switch state
                }
            } catch (e: NumberFormatException) {
                Log.e("TruckJourneyVM", "Error parsing numbers during save", e)
                // Handle parsing error, maybe update UI
            }
        } else {
            Log.w("TruckJourneyVM", "No expanded journey data to save.")
        }
    }
}