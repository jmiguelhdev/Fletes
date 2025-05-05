package com.example.fletes.ui.screenDispatch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fletes.data.room.Destino
import com.example.fletes.domain.DeleteDestinoUseCase
import com.example.fletes.domain.GetActiveDestinosUseCase
import com.example.fletes.domain.GetActiveDispatchCount
import com.example.fletes.domain.GetAllDestinosUseCase
import com.example.fletes.domain.InsertDestinoUseCase
import com.example.fletes.domain.SearchComisionistaUseCase
import com.example.fletes.domain.SearchLocalidadUseCase
import com.example.fletes.domain.UpdateDestinoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
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

) {
    val isFormValid: Boolean
        get() = isValidComisionista && isValidLocalidad && isValidDespacho
                && comisionista.isNotBlank() && localidad.isNotBlank() && despacho >= 0
}



class NewDispatchViewModel(
    getActiveDispatch: GetActiveDestinosUseCase,
    getActiveDispatchCount: GetActiveDispatchCount,
    private val searchComisionistaUseCase: SearchComisionistaUseCase,
    private val searchLocalidadUseCase: SearchLocalidadUseCase,
    private val getAllDestinosUseCase: GetAllDestinosUseCase,
    private val insertDestinoUseCase: InsertDestinoUseCase,
    private val deleteDestinoUseCase: DeleteDestinoUseCase,
    private val updateDestinoUseCase: UpdateDestinoUseCase,
) : ViewModel() {


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
}