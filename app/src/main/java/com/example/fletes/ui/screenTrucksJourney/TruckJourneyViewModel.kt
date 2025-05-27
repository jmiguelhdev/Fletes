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
        viewModelScope.launch {
            updateTruckIsActive(camion)
        }
    }

    // Funciones para actualizar cada valor y guardar en SavedStateHandle
    private fun updateKmCargaValue(newValue: String) {
        // Validacion
        var error = ""
        try {
            newValue.toDouble()
        } catch (e: NumberFormatException) {
            error = "debe ser un numero $e"
        }
        savedStateHandle[KM_CARGA_VALUE] = newValue
        savedStateHandle[KM_CARGA_ERROR] = error
        _truckJourneyUiState.update {
            it.copy(
                newJourneyFormData = it.newJourneyFormData.copy(
                    kmCargaData = it.newJourneyFormData.kmCargaData.copy(
                        value = newValue,
                        errorMessage = error
                    )
                )
            )
        }
    }

    private fun updateKmDescargaValue(newValue: String) {
        // Validacion
        var error = ""
        try {
            newValue.toDouble()
        } catch (e: NumberFormatException) {
            error = "debe ser un numero $e"
        }
        savedStateHandle[KM_DESCARGA_VALUE] = newValue
        savedStateHandle[KM_DESCARGA_ERROR] = error
        _truckJourneyUiState.update {
            it.copy(
                newJourneyFormData = it.newJourneyFormData.copy(
                    kmDescargaData = it.newJourneyFormData.kmDescargaData.copy(
                        value = newValue,
                        errorMessage = error
                    )
                )
            )
        }
    }

    private fun updateKmSurtidorValue(newValue: String) {
        // Validacion
        var error = ""
        try {
            newValue.toDouble()
        } catch (e: NumberFormatException) {
            error = "debe ser un numero $e"
        }
        savedStateHandle[KM_SURTIDOR_VALUE] = newValue
        savedStateHandle[KM_SURTIDOR_ERROR] = error
        _truckJourneyUiState.update {
            it.copy(
                newJourneyFormData = it.newJourneyFormData.copy(
                    kmSurtidorData = it.newJourneyFormData.kmSurtidorData.copy(
                        value = newValue,
                        errorMessage = error
                    )
                )
            )
        }
    }

    private fun updateLitrosValue(newValue: String) {
        // Validacion
        var error = ""
        try {
            newValue.toDouble()
        } catch (e: NumberFormatException) {
            error = "debe ser un numero $e"
        }
        savedStateHandle[LITROS_VALUE] = newValue
        savedStateHandle[LITROS_ERROR] = error
        _truckJourneyUiState.update {
            it.copy(
                newJourneyFormData = it.newJourneyFormData.copy(
                    litrosData = it.newJourneyFormData.litrosData.copy(
                        value = newValue,
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
            Log.d("DispatchViewModel", "updateTruckIsActive: $truck")
            _truckJourneyUiState.update { currentState ->
                currentState.copy(
                    newJourneyTruckSelected = truck.copy(
                        isActive = false
                    ),
                )
            }

        }
    }

    private val _allJourneys = MutableStateFlow<List<JourneyWithAllDetails>>(emptyList())
    val allJourneys: StateFlow<List<JourneyWithAllDetails>> = _allJourneys.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    //distancia debe ser unico para cada registro
// This function will be called by onCheckedChange and init
    fun observeJourneys() {
        viewModelScope.launch {
            _truckJourneyUiState.update { it.copy(isLoading = true, isError = false) }
            try {
                // Assuming getAllJourneysWithAllDetailsUseCase can filter by active status
                // or you have a different use case for active/inactive.
                // For this example, let's assume it returns all and we might filter later if needed,
                // or better, the use case handles it.
                getAllJourneysWithAllDetails() // Or a specific use case for active/all
                    .map { journeysList ->
                        journeysList.map { journeyWithDetails ->
                            // Calculate distance for each journey
                            val individualDistance =
                                journeyWithDetails.journey.getDistancia() // Your existing method
                            // Create a new instance or update the existing one with the distance
                            journeyWithDetails.copy(calculatedDistance = individualDistance)
                        }
                    }
                    .onStart { Log.d("TruckJourneyVM", "Starting to load journeys...") }
                    .onCompletion {
                        if (it == null) Log.d(
                            "TruckJourneyVM",
                            "Journey loading completed."
                        )
                    }

                    .collect { journeysWithDistance ->
                        Log.d(
                            "TruckJourneyVM",
                            "Journeys with distance: ${journeysWithDistance.size}"
                        )
                        _truckJourneyUiState.update {
                            it.copy(
                                isLoading = false,
                                journeysForDisplay = journeysWithDistance // Update the UI state
                            )
                        }
                    }
            } catch (e: Exception) { // Catch exceptions from launching the flow collection itself
                Log.e("TruckJourneyVM", "Error launching journey observation", e)
                _truckJourneyUiState.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }

    init {
        observeJourneys()
    }

    fun loadJourneys() {
        Log.d("TruckJourneyViewModel", "loadJourneys called")
        viewModelScope.launch {
            _truckJourneyUiState.update { it.copy(isLoading = true, isError = false) }
            try {
                Log.d("TruckJourneyViewModel", "Attempting to collect from getAllJourneyUseCase")
                getAllJourneysWithAllDetails()
                    .onStart {
                        Log.d("TruckJourneyViewModel", "getAllJourneyUseCase Flow started")
                    }
                    .onCompletion { cause ->
                        if (cause != null && cause !is kotlinx.coroutines.CancellationException) {
                            Log.e("TruckJourneyViewModel", "Flow completed with error", cause)
                        } else if (cause is kotlinx.coroutines.CancellationException) {
                            Log.w("TruckJourneyViewModel", "Flow collection was cancelled", cause)
                        } else {
                            Log.d("TruckJourneyViewModel", "Flow collection completed successfully")
                        }
                    }
                    .collect { journeys ->
                        Log.d("TruckJourneyViewModel", "Collected journeys: $journeys")
                        _allJourneys.value = journeys
                        _truckJourneyUiState.update { it.copy(isLoading = false) }
                        Log.d(
                            "TruckJourneyViewModel",
                            "isLoading set to false. Current _allJourneys: ${_allJourneys.value}"
                        )
                    }
            } catch (e: Exception) {
                Log.e("TruckJourneyViewModel", "Error loading journeys in try-catch", e)
                _truckJourneyUiState.update { it.copy(isLoading = false, isError = true) }
            } finally {
                Log.d("TruckJourneyViewModel", "Coroutine in loadJourneys finished.")

            }
        }
    }

    private val _activeJourneys = MutableStateFlow<List<JourneyWithAllDetails>>(emptyList())
    val activeJourneys: StateFlow<List<JourneyWithAllDetails>> = _activeJourneys.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    fun loadActiveJourneys() {
        Log.d("TruckJourneyViewModel", "loadJourneys called")
        viewModelScope.launch {
            _truckJourneyUiState.update { it.copy(isLoading = true, isError = false) }
            try {
                Log.d("TruckJourneyViewModel", "Attempting to collect from getAllJourneyUseCase")
                getActiveJourneysWithAllDetails()
                    .onStart {
                        Log.d("TruckJourneyViewModel", "getAllJourneyUseCase Flow started")
                    }
                    .onCompletion { cause ->
                        if (cause != null && cause !is kotlinx.coroutines.CancellationException) {
                            Log.e("TruckJourneyViewModel", "Flow completed with error", cause)
                        } else if (cause is kotlinx.coroutines.CancellationException) {
                            Log.w("TruckJourneyViewModel", "Flow collection was cancelled", cause)
                        } else {
                            Log.d("TruckJourneyViewModel", "Flow collection completed successfully")
                        }
                    }
                    .collect { journeys ->
                        Log.d("TruckJourneyViewModel", "Collected journeys: $journeys")
                        _activeJourneys.value = journeys
                        _truckJourneyUiState.update { it.copy(isLoading = false) }
                        Log.d(
                            "TruckJourneyViewModel",
                            "isLoading set to false. Current _allJourneys: ${_allJourneys.value}"
                        )
                    }
            } catch (e: Exception) {
                Log.e("TruckJourneyViewModel", "Error loading journeys in try-catch", e)
                _truckJourneyUiState.update { it.copy(isLoading = false, isError = true) }
            } finally {
                Log.d("TruckJourneyViewModel", "Coroutine in loadJourneys finished.")

            }
        }
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

    // For NEW JOURNEY (existing functions - no change needed to their internals)
// private fun updateKmCargaValue(newValue: String) { ... }
// private fun updateKmDescargaValue(newValue: String) { ... }
// ... and so on
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
            var error = ""
            try {
                newValue.toDouble() // Basic validation
            } catch (e: NumberFormatException) {
                error = "Debe ser un número $e"
            }

            _truckJourneyUiState.update { currentState ->
                val updatedEditableData = currentEditableData.copy(
                    kmCargaData = currentEditableData.kmCargaData.copy(
                        value = newValue,
                        errorMessage = error
                    )
                )

                // Automatically update isActive based on completeness
                // If you want isActive to be FALSE when complete:
                val newIsActiveState = !isEditableExpandedJourneyComplete(updatedEditableData)
                // If you want isActive to be TRUE when complete:
                // val newIsActiveState = isEditableExpandedJourneyComplete(updatedEditableData)

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
            var error = ""
            try {
                newValue.toDouble()
            } catch (e: NumberFormatException) {
                error = "Debe ser un número $e"
            }
            _truckJourneyUiState.update { currentState ->
                val updatedEditableData = currentEditableData.copy(
                    kmDescargaData = currentEditableData.kmDescargaData.copy(
                        value = newValue,
                        errorMessage = error
                    )
                )

                // Automatically update isActive based on completeness
                // If you want isActive to be FALSE when complete:
                val newIsActiveState = !isEditableExpandedJourneyComplete(updatedEditableData)
                // If you want isActive to be TRUE when complete:
                // val newIsActiveState = isEditableExpandedJourneyComplete(updatedEditableData)

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
            var error = ""
            try {
                newValue.toDouble()
            } catch (e: NumberFormatException) {
                error = "Debe ser un número $e"
            }
            _truckJourneyUiState.update { currentState ->
                val updatedEditableData = currentEditableData.copy(
                    kmSurtidorData = currentEditableData.kmSurtidorData.copy(
                        value = newValue,
                        errorMessage = error
                    )
                )

                // Automatically update isActive based on completeness
                // If you want isActive to be FALSE when complete:
                val newIsActiveState = !isEditableExpandedJourneyComplete(updatedEditableData)
                // If you want isActive to be TRUE when complete:
                // val newIsActiveState = isEditableExpandedJourneyComplete(updatedEditableData)

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
            var error = ""
            try {
                newValue.toDouble()
            } catch (e: NumberFormatException) {
                error = "Debe ser un número $e"
            }
            _truckJourneyUiState.update { currentState ->
                val updatedEditableData = currentEditableData.copy(
                    litrosData = currentEditableData.litrosData.copy(
                        value = newValue,
                        errorMessage = error
                    )
                )

                // Automatically update isActive based on completeness
                // If you want isActive to be FALSE when complete:
                val newIsActiveState = !isEditableExpandedJourneyComplete(updatedEditableData)
                // If you want isActive to be TRUE when complete:
                // val newIsActiveState = isEditableExpandedJourneyComplete(updatedEditableData)

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
                    loadJourneys() // Refresh the list to show updated data
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