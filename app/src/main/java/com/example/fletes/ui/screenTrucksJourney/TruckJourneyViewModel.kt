package com.example.fletes.ui.screenTrucksJourney

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fletes.data.model.DecimalTextFieldData
import com.example.fletes.data.model.truckJourneyData.TruckJourneyData
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.CamionesRegistro
import com.example.fletes.data.room.Destino
import com.example.fletes.domain.GetAllJourneyUseCase
import com.example.fletes.domain.GetDestinationByIdUseCase
import com.example.fletes.domain.GetTruckByIdUseCase
import com.example.fletes.domain.GetTruckJourneyByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


data class TruckJourneyUiState(
    val truckJourneyData: TruckJourneyData = TruckJourneyData(
        camionId  = 0,
        kmCargaData = DecimalTextFieldData("km carga", "", {}, ""),
        kmDescargaData = DecimalTextFieldData("km descarga", "", {}, ""),
        kmSurtidorData = DecimalTextFieldData("km surtidor", "", {}, ""),
        litrosData = DecimalTextFieldData("litros surtidos", "", {}, ""),
        isActive = false
    ),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
        val truckSelected: Camion = Camion(
        choferName = "",
        choferDni = 0,
        patenteTractor = "",
        patenteJaula = "",
        isActive = true
    ),
    val destinationSelected: Destino = Destino(
        comisionista = "",
        despacho = 0.0,
        localidad = "",
        isActive = true
    ),
    val expandedJourneyId: Int? = null,
    val expandedJourneyDetails: CamionesRegistro? = null
)


class TruckJourneyViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getAllJourneyUseCase: GetAllJourneyUseCase,
    private val getTruckByIdUseCase: GetTruckByIdUseCase,
    private val getDestinationByIdUseCase: GetDestinationByIdUseCase,
    private val getTruckJourneyByIdUseCase: GetTruckJourneyByIdUseCase,
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
            truckJourneyData = TruckJourneyData(
                camionId  = savedStateHandle[TRUCK_SELECTED_ID] ?: 0,
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
        } catch (e: NumberFormatException){
            error = "debe ser un numero $e"
        }
        savedStateHandle[KM_CARGA_VALUE] = newValue
        savedStateHandle[KM_CARGA_ERROR] = error
        _truckJourneyUiState.update {
            it.copy(truckJourneyData = it.truckJourneyData.copy(kmCargaData = it.truckJourneyData.kmCargaData.copy(value = newValue, errorMessage = error)))
        }
    }

    private fun updateKmDescargaValue(newValue: String) {
        // Validacion
        var error = ""
        try {
            newValue.toDouble()
        } catch (e: NumberFormatException){
            error = "debe ser un numero $e"
        }
        savedStateHandle[KM_DESCARGA_VALUE] = newValue
        savedStateHandle[KM_DESCARGA_ERROR] = error
        _truckJourneyUiState.update {
            it.copy(truckJourneyData = it.truckJourneyData.copy(kmDescargaData = it.truckJourneyData.kmDescargaData.copy(value = newValue, errorMessage = error)))
        }
    }

    private fun updateKmSurtidorValue(newValue: String) {
        // Validacion
        var error = ""
        try {
            newValue.toDouble()
        } catch (e: NumberFormatException){
            error = "debe ser un numero $e"
        }
        savedStateHandle[KM_SURTIDOR_VALUE] = newValue
        savedStateHandle[KM_SURTIDOR_ERROR] = error
        _truckJourneyUiState.update {
            it.copy(truckJourneyData = it.truckJourneyData.copy(kmSurtidorData = it.truckJourneyData.kmSurtidorData.copy(value = newValue, errorMessage = error)))
        }
    }

    private fun updateLitrosValue(newValue: String) {
        // Validacion
        var error = ""
        try {
            newValue.toDouble()
        } catch (e: NumberFormatException){
            error = "debe ser un numero $e"
        }
        savedStateHandle[LITROS_VALUE] = newValue
        savedStateHandle[LITROS_ERROR] = error
        _truckJourneyUiState.update {
            it.copy(truckJourneyData = it.truckJourneyData.copy(litrosData = it.truckJourneyData.litrosData.copy(value = newValue, errorMessage = error)))
        }
    }

    fun updateIsActiveValue(newValue: Boolean) {
        savedStateHandle[IS_ACTIVE_VALUE] = newValue
        _truckJourneyUiState.update {
            it.copy(truckJourneyData = it.truckJourneyData.copy(isActive = newValue))
        }
    }
    fun updateTruckIsActive(truck: Camion) {
        viewModelScope.launch {
            Log.d("DispatchViewModel", "updateTruckIsActive: $truck")
            _truckJourneyUiState.update { currentState ->
                currentState.copy(
                    truckSelected = truck.copy(
                        isActive = false
                    ),
                )
            }

        }
    }

    private val _allJourneys = MutableStateFlow<List<CamionesRegistro>>(emptyList())
    val allJourneys: StateFlow<List<CamionesRegistro>> = _allJourneys.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )



    fun loadJourneys() {
        Log.d("TruckJourneyViewModel", "loadJourneys called")
        viewModelScope.launch {
            _truckJourneyUiState.update { it.copy(isLoading = true, isError = false) }
            try {
                Log.d("TruckJourneyViewModel", "Attempting to collect from getAllJourneyUseCase")
                getAllJourneyUseCase()
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
                        Log.d("TruckJourneyViewModel", "isLoading set to false. Current _allJourneys: ${_allJourneys.value}")
                    }
            } catch (e: Exception) {
                Log.e("TruckJourneyViewModel", "Error loading journeys in try-catch", e)
                _truckJourneyUiState.update { it.copy(isLoading = false, isError = true) }
            } finally {
                Log.d("TruckJourneyViewModel", "Coroutine in loadJourneys finished.")
            }
        }
    }



    fun onClickJourneyCard(journeyId: Int) {
        viewModelScope.launch {
            val currentUiState = _truckJourneyUiState.value

            // If this card is already expanded, collapse it
            if (currentUiState.expandedJourneyId == journeyId) {
                _truckJourneyUiState.update {
                    it.copy(
                        expandedJourneyId = null,
                        expandedJourneyDetails = null
                    )
                }
                Log.d("TruckJourneyViewModel", "Collapsed journey ID: $journeyId")
            } else {
                // Otherwise, expand this card and fetch its details
                _truckJourneyUiState.update {
                    it.copy(
                        isLoading = true, // Show loading for detail fetch
                        expandedJourneyId = journeyId, // Set the new expanded ID
                        expandedJourneyDetails = null // Clear previous details
                    )
                }
                Log.d("TruckJourneyViewModel", "Expanding journey ID: $journeyId. Fetching details...")
                try {
                    // Fetch details for the newly expanded journey
                    // Assuming getTruckJourneyByIdUseCase returns Flow<CamionesRegistro?>
                    val journeyDetailsFlow = getTruckJourneyByIdUseCase(journeyId)
                    val journeyDetails = journeyDetailsFlow.firstOrNull() // Get the first emission

                    if (journeyDetails != null) {
                        Log.d("TruckJourneyViewModel", "Successfully fetched details for $journeyId: $journeyDetails")
                        val selectedTruck = getTruckByIdUseCase(journeyDetails.camionId)
                        val selectedDestinationFlow = getDestinationByIdUseCase(journeyDetails.destinoId)
                        val selectedDestination = selectedDestinationFlow.firstOrNull()
                        _truckJourneyUiState.update {
                            it.copy(
                                isLoading = false,
                                expandedJourneyDetails = journeyDetails,
                                truckSelected = selectedTruck!!,
                                destinationSelected = selectedDestination!!,
                                // Potentially fetch related Truck and Destination details here if needed
                                // and if they are not part of CamionesRegistro or a combined DTO
                            )
                        }
                    } else {
                        Log.w("TruckJourneyViewModel", "No details found for journey ID: $journeyId")
                        _truckJourneyUiState.update {
                            it.copy(
                                isLoading = false,
                                isError = true, // Or a specific error message for not found
                                expandedJourneyId = null // Collapse if details not found
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e("TruckJourneyViewModel", "Error fetching journey details for ID: $journeyId", e)
                    _truckJourneyUiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            expandedJourneyId = null // Collapse on error
                        )
                    }
                }
            }
        }
    }
}