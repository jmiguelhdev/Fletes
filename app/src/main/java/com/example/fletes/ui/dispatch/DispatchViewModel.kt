package com.example.fletes.ui.destino

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fletes.data.repositories.interfaces.DestinationRepositoryInterface
import com.example.fletes.data.room.Destino
import com.example.fletes.domain.GetAllDestinosUseCase
import com.example.fletes.domain.InsertDestinoUseCase
import com.example.fletes.domain.SearchComisionistaUseCase
import com.example.fletes.domain.SearchLocalidadUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DispatchUiState(
    val comisionista: String = "",
    val comisionistaErrorMessage: String? = null,
    val isValidComisionista: Boolean = true,
    val localidad: String = "",
    val localidadErrorMessage: String? = null,
    val isValidLocalidad: Boolean = true,
    val showInsertDialog: Boolean = false,
    val comisionistaSuggestions: List<String> = emptyList(),
    val localidadSuggestions: List<String> = emptyList(),
    val isInsertButtonEnabled: Boolean = false,
    val isLoading: Boolean = false
)

class DispatchViewModel(
    private val destinationRepository: DestinationRepositoryInterface,
    private val searchComisionistaUseCase: SearchComisionistaUseCase,
    private val searchLocalidadUseCase: SearchLocalidadUseCase,
    private val getAllDestinosUseCase: GetAllDestinosUseCase,
    private val insertDestinoUseCase: InsertDestinoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DispatchUiState())
    val uiState: StateFlow<DispatchUiState> = _uiState.asStateFlow()

    // Comisionista StateFlows
    private val _comisionistaQuery = MutableStateFlow("")
    // Localidad StateFlows
    private val _localidadQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            combineSuggestions()
            loadInitialData()
        }
    }
    private fun loadInitialData(){
        loadComisionistasAndLocalidades()
    }
    private fun loadComisionistasAndLocalidades() {
        viewModelScope.launch {
            getAllDestinosUseCase().catch {
                // Handle any exception during data loading
                Log.e("DispatchViewModel", "Error loading data", it)
            }.collect { allDestinos ->
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

    private fun combineSuggestions() {
        viewModelScope.launch {
            _comisionistaQuery.flatMapLatest { query ->
                Log.d("DispatchViewModel", "Localidad query changed: $query")
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
            Log.d("DispatchViewModel", "Comisionista suggestions updated")
            _localidadQuery.flatMapLatest { query ->
                if (query.isBlank()) {
                    flowOf(emptyList())
                } else {
                    searchLocalidadUseCase(query)
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
        combineSuggestions()
    }

     fun onLocalidadQueryChange(query: String) {
        _localidadQuery.update { query }
        combineSuggestions()
    }

    // Insert new Destino
    fun insertNewDestino() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        if (_uiState.value.isValidComisionista && _uiState.value.isValidLocalidad) {
            viewModelScope.launch {
                insertDestinoUseCase(
                    Destino(
                        comisionista = _uiState.value.comisionista,
                        localidad = _uiState.value.localidad
                    )
                )
                _uiState.update {
                    it.copy(isLoading = false, showInsertDialog = false)
                }
            }
        } else {
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    // Update UiState
    fun updateComisionista(value: String) {
        _uiState.update { currentState ->
            currentState.copy(comisionista = value)
        }
        validateComisionista(value)
        updateInsertButton()
    }

    fun updateLocalidad(value: String) {
        _uiState.update { currentState ->
            currentState.copy(localidad = value)
        }
        validateLocalidad(value)
        updateInsertButton()
    }

    fun showInsertDialog(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(showInsertDialog = value)
        }
    }

    fun validateComisionista(value: String) {
        _uiState.update { currentState ->
            if (value.isBlank()) {
                currentState.copy(
                    isValidComisionista = false,
                    comisionistaErrorMessage = "Debe ingresar un comisionista"
                )
            } else {
                currentState.copy(isValidComisionista = true, comisionistaErrorMessage = null)
            }
        }
    }

    fun validateLocalidad(value: String) {
        _uiState.update { currentState ->
            if (value.isBlank()) {
                currentState.copy(
                    isValidLocalidad = false,
                    localidadErrorMessage = "Debe ingresar una localidad"
                )
            } else {
                currentState.copy(isValidLocalidad = true, localidadErrorMessage = null)
            }
        }
    }

    private fun updateInsertButton() {
        _uiState.update { currentState ->
            currentState.copy(
                isInsertButtonEnabled = currentState.isValidComisionista && currentState.isValidLocalidad
            )
        }
    }
}