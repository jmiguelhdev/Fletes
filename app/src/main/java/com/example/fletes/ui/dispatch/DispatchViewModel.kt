package com.example.fletes.ui.dispatch

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fletes.data.model.DecimalTextFieldData
import com.example.fletes.data.model.truckJourneyData.TruckJourneyData
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.CamionesRegistro
import com.example.fletes.data.room.Destino
import com.example.fletes.domain.DeleteDestinoUseCase
import com.example.fletes.domain.GetActiveDestinosUseCase
import com.example.fletes.domain.GetActiveDispatchCount
import com.example.fletes.domain.GetActiveTrucks
import com.example.fletes.domain.GetAllDestinosUseCase
import com.example.fletes.domain.GetAllJourneyUseCase
import com.example.fletes.domain.GetAllTrucks
import com.example.fletes.domain.InsertDestinoUseCase
import com.example.fletes.domain.InsertJourneyUseCase
import com.example.fletes.domain.SearchComisionistaUseCase
import com.example.fletes.domain.SearchLocalidadUseCase
import com.example.fletes.domain.UpdateDestinoUseCase
import com.example.fletes.domain.UpdateTruckIsActiveUseCase
import com.example.fletes.domain.UpdateJourneyUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DispatchUiState(
    val isActive: Boolean = true,
    val comisionista: String = "",
    val comisionistaErrorMessage: String? = null,
    val isValidComisionista: Boolean = true,
    val despacho: Double = 0.0,
    val isValidDespacho: Boolean = true,
    val despachoErrorMessage: String? = null,
    val localidad: String = "",
    val localidadErrorMessage: String? = null,
    val isValidLocalidad: Boolean = true,
    val showDeleteDialog: Boolean = false,
    val showUpdateDialog: Boolean = false,
    val comisionistaSuggestions: List<String> = emptyList(),
    val localidadSuggestions: List<String> = emptyList(),
    val isInsertButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val showSnackbar: Boolean = false,
    val snackbarMessage: String = "",
    //creo que no a aqui, creare otro viewModel para el trucks detail
    val truckSelected: Camion = Camion(
        choferName = "",
        choferDni = 0,
        patenteTractor = "",
        patenteJaula = "",
        isActive = true
    )
) {
    val isFormValid: Boolean
        get() = isValidComisionista && isValidLocalidad && isValidDespacho
                && comisionista.isNotBlank() && localidad.isNotBlank() && despacho >= 0
}

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
)

class DispatchViewModel(
    getActiveDispatch: GetActiveDestinosUseCase,
    getActiveDispatchCount: GetActiveDispatchCount,
    getActiveTrucks: GetActiveTrucks,
    getAllJourneys: GetAllJourneyUseCase,
    private val insertJourneyUseCase: InsertJourneyUseCase,
    private val searchComisionistaUseCase: SearchComisionistaUseCase,
    private val searchLocalidadUseCase: SearchLocalidadUseCase,
    private val getAllDestinosUseCase: GetAllDestinosUseCase,
    private val insertDestinoUseCase: InsertDestinoUseCase,
    private val deleteDestinoUseCase: DeleteDestinoUseCase,
    private val updateDestinoUseCase: UpdateDestinoUseCase,
    private val updateJourneyUseCase: UpdateJourneyUseCase,
    private val updateTruckIsActiveUseCase: UpdateTruckIsActiveUseCase,
    private val savedStateHandle: SavedStateHandle
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
    fun updateTruckIsActive(truck: Camion) {
        viewModelScope.launch {
            Log.d("DispatchViewModel", "updateTruckIsActive: $truck")
            _uiState.update { currentState ->
                currentState.copy(
                    truckSelected = truck.copy(
                        isActive = false
                    ),
                )
            }
            updateTruckIsActiveUseCase(truck)
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



    private val _uiState = MutableStateFlow(DispatchUiState())
    val uiState: StateFlow<DispatchUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = DispatchUiState()
    )

    val activeDispatch: StateFlow<List<Destino>> =
        getActiveDispatch().stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000), // Consider shorter timeout if feasible
            initialValue = emptyList()
        )

    val activeDispatchCount: StateFlow<Int> =
        getActiveDispatchCount().stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000), // Consider shorter timeout if feasible
            initialValue = 0
        )

    val allActiveTrucks: StateFlow<List<Camion>> = getActiveTrucks().stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000), // Consider shorter timeout if feasible
        initialValue = emptyList()
    )


     val allJourneys = getAllJourneys().stateIn(
         scope = viewModelScope,
         started = WhileSubscribed(5000),
         initialValue = emptyList()
     )

    // Comisionista StateFlows
    private val _comisionistaQuery = MutableStateFlow("")

    // Localidad StateFlows
    private val _localidadQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            loadInitialData()
        }
    }

    private fun loadInitialData() {
        loadComisionistasAndLocalidades()
    }

    private fun loadComisionistasAndLocalidades() {
        viewModelScope.launch {
            getAllDestinosUseCase().catch {
                // Handle any exception during data loading
                Log.e("DispatchViewModel", "Error loading data", it)
            }
                .collect { allDestinos ->
                    val comisionistas = allDestinos.map { it.comisionista }.distinct()
                    val localidades = allDestinos.map { it.localidad }.distinct()
                    Log.d("DispatchViewModel", "Loaded comisionistas: $comisionistas")
                    Log.d("DispatchViewModel", "Loaded localidades: $localidades")
                    _uiState.update { currentState ->
                        currentState.copy(
                            comisionistaSuggestions = comisionistas,
                            localidadSuggestions = localidades
                        )
                    }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun combineComisionistaSuggestions() {
        viewModelScope.launch {
            _comisionistaQuery.flatMapLatest { query ->
                Log.d("DispatchViewModel", "Comisionista query changed: $query")
                if (query.isBlank()) {
                    flowOf(emptyList())
                } else {
                    searchComisionistaUseCase(query)
                }
            }.collect { suggestions ->
                _uiState.update { currentState ->
                    currentState.copy(comisionistaSuggestions = suggestions)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun combineLocalidadSuggestions() {
        viewModelScope.launch {
            _localidadQuery.flatMapLatest { queryLocalidad ->
                Log.d("DispatchViewModel", "Localidad query changed: $queryLocalidad")
                if (queryLocalidad.isBlank()) {
                    flowOf(emptyList())
                } else {
                    searchLocalidadUseCase(queryLocalidad)
                }
            }.collect { suggestions ->
                _uiState.update { currentState ->
                    currentState.copy(localidadSuggestions = suggestions)
                }
            }
        }
    }


    fun onComisionistaQueryChange(query: String) {
        _comisionistaQuery.update { query }
        combineComisionistaSuggestions()
    }

    fun onLocalidadQueryChange(query: String) {
        _localidadQuery.update { query }
        combineLocalidadSuggestions()
    }

    // Insert new Destino
    fun insertNewDestino() {
        if (!_uiState.value.isFormValid) {
            _uiState.update {
                it.copy(isLoading = false)
            }
            return
        }
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            try {
                insertDestinoUseCase(
                    Destino(
                        comisionista = _uiState.value.comisionista,
                        localidad = _uiState.value.localidad,
                        despacho = _uiState.value.despacho
                    )
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        showDeleteDialog = false,
                        showSnackbar = true,
                        snackbarMessage = "Destino insertado correctamente"
                    )
                }
            } catch (e: Exception) {
                // Handle specific exceptions here
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        showSnackbar = true,
                        snackbarMessage = "Error al insertar el destino: ${e.message}" // Or a generic error message
                    )
                }
            }

        }
    }

    fun snackbarShown() {
        _uiState.update {
            it.copy(showSnackbar = false, snackbarMessage = "")
        }
    }

    // Update UiState
    fun onValueChangeComisionista(value: String) {
        _uiState.update { currentState ->
            currentState.copy(comisionista = value)
        }
        validateComisionista(value)
        updateInsertButton()
    }

    fun onValueChangeDespacho(despacho: String) {
        val cleanDespacho = despacho.replace(",", ".")
        _uiState.update { currentState ->
            var newDespacho: Double? = null
            var errorMessage: String? = null
            var isValid = true
            if (cleanDespacho.isNotEmpty()) {
                try {
                    newDespacho = cleanDespacho.toDouble()
                    isValid = true
                } catch (e: NumberFormatException) {
                    errorMessage = "Debe ingresar un número válido $e"
                    isValid = false
                    newDespacho = null
                }
            }
            currentState.copy(
                despacho = newDespacho ?: 0.0,
                isValidDespacho = isValid,
                despachoErrorMessage = errorMessage
            )
        }
        Log.d("DispatchViewModel", "onValueChangeDespacho: $despacho")
        updateInsertButton()
    }

    fun onValueChangeLocalidad(value: String) {
        _uiState.update { currentState ->
            currentState.copy(localidad = value)
        }
        validateLocalidad(value)
        updateInsertButton()
    }


    private fun validateComisionista(value: String) {
        _uiState.update { currentState ->
            val isValid = value.isNotBlank()
            val errorMessage = if (!isValid) "Debe ingresar un comisionista" else null
            currentState.copy(
                isValidComisionista = isValid,
                comisionistaErrorMessage = errorMessage
            )
        }
    }

    private fun validateLocalidad(value: String) {
        _uiState.update { currentState ->
            val isValid = value.isNotBlank()
            val errorMessage = if (!isValid) "Debe ingresar una localidad" else null
            currentState.copy(
                isValidLocalidad = isValid,
                localidadErrorMessage = errorMessage
            )
        }
    }

    private fun updateInsertButton() {
        _uiState.update { currentState ->
            currentState.copy(
                isInsertButtonEnabled = currentState.isFormValid
            )
        }
    }

    fun showDeleteDialog() {
        _uiState.update { currentState ->
            currentState.copy(showDeleteDialog = true)
        }
    }

    fun hideDeleteDialog() {
        _uiState.update { currentState ->
            currentState.copy(showDeleteDialog = false)
        }
    }
    fun showUpdateDialog() {
        _uiState.update { currentState ->
            currentState.copy(showUpdateDialog = true)
        }
    }

    fun hideUpdateDialog() {
        _uiState.update { currentState ->
            currentState.copy(showUpdateDialog = false)
        }
    }

    fun deleteDetino(destino: Destino) {
        viewModelScope.launch {
            try {
                deleteDestinoUseCase(destino)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        showSnackbar = true,
                        snackbarMessage = "Error al eliminar el destino: ${e.message}"
                    )
                }
            }
        }
    }

    fun updateDestination(destino: Destino) {
        viewModelScope.launch {
            Log.d("DispatchViewModel", "pre update ${destino.despacho}")
            try {
               val  updatedDestino = destino.copy(
                    despacho = uiState.value.despacho
                )
                Log.d("DispatchViewModel", "updatedDestino: $updatedDestino")

                updateDestinoUseCase(updatedDestino)

                _uiState.update {
                    it.copy(
                        showSnackbar = true,
                        snackbarMessage = "Destino editado correctamente",
                        showUpdateDialog = false
                    )
                }
                Log.d("DispatchViewModel", "post update ${destino.despacho}")
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        showSnackbar = true,
                        snackbarMessage = "Error al eliminar el destino: ${e.message}"
                    )
                }
            }
        }
    }



    fun saveJourney(desinoId: Int){
        viewModelScope.launch {
            val truckSelectedId: Int? = savedStateHandle[TRUCK_SELECTED_ID]
            Log.d("DispatchViewModel", "all journeys: ${allJourneys.value}")
            try {
                val existingJourney = allJourneys.value.firstOrNull {
                    it.destinoId == desinoId && it.camionId == truckSelectedId && it.isActive == true
                }
                Log.d("DispatchViewModel", "existingJourney: $existingJourney")
                val existingJourneyId = existingJourney?.id
                activeDispatch.value.first { it.id == desinoId }.let {
                    val truckId = truckSelectedId ?: 0
                    val journeyToUpdate = CamionesRegistro(
                        id = existingJourneyId ?: 0,
                        camionId = truckId,
                        destinoId = desinoId,
                        kmCarga = truckJourneyUiState.value.truckJourneyData.kmCargaData.value.toIntOrNull() ?: 0,
                        kmDescarga = truckJourneyUiState.value.truckJourneyData.kmDescargaData.value.toIntOrNull() ?: 0,
                        kmSurtidor = truckJourneyUiState.value.truckJourneyData.kmSurtidorData.value.toIntOrNull() ?: 0,
                        litros = truckJourneyUiState.value.truckJourneyData.litrosData.value.toDoubleOrNull() ?: 0.0,
                    )

                    if (existingJourney == null) {
                        //allJorneys no existe
                        insertJourneyUseCase(journeyToUpdate)
                        updateDestinoUseCase(it.copy(isActive = false))
                        Log.d("DispatchViewModel", "insertJourneyUseCase: $journeyToUpdate")
                    } else {
                        updateJourneyUseCase(journeyToUpdate)
                        updateDestinoUseCase(it.copy(isActive = false))
                        Log.d("DispatchViewModel", "updateJourneyUseCase: $journeyToUpdate")
                    }

                    _uiState.update { currentState ->
                        currentState.copy(
                            showSnackbar = true,
                            snackbarMessage = "Registro guardado correctamente",
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        showSnackbar = true, snackbarMessage = "Error al guardar el registro: ${e.message}"
                    )
            }
            }
        }
    }
}