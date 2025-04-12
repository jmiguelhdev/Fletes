package com.example.fletes.ui.destinations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fletes.data.room.Destino
import com.example.fletes.domain.GetAllDestinosUseCase
import com.example.fletes.domain.InsertDestinoUseCase
import com.example.fletes.domain.SearchComisionistaUseCase
import com.example.fletes.domain.SearchLocalidadUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class DestinoViewModel(
    private val searchComisionistaUseCase: SearchComisionistaUseCase,
    private val searchLocalidadUseCase: SearchLocalidadUseCase,
    private val getAllDestinosUseCase: GetAllDestinosUseCase,
    private val insertDestinoUseCase: InsertDestinoUseCase
) : ViewModel() {
    // Search state
    private val _comisionistaQuery = MutableStateFlow("")
    val comisionistaQuery: StateFlow<String> = _comisionistaQuery.asStateFlow()

    private val _localidadQuery = MutableStateFlow("")
    val localidadQuery: StateFlow<String> = _localidadQuery.asStateFlow()

    // Search results
    val comisionistas: StateFlow<List<String>> = _comisionistaQuery
        .combine(getAllDestinosUseCase()) { query, allDestinos ->
            if (query.isBlank()) {
                emptyList()
            } else {
                allDestinos.map { it.comisionista }.filter { it.contains(query, ignoreCase = true) }.distinct()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val localidades: StateFlow<List<String>> = _localidadQuery
        .combine(getAllDestinosUseCase()) { query, allDestinos ->
            if (query.isBlank()) {
                emptyList()
            } else {
                allDestinos.map { it.localidad }.filter { it.contains(query, ignoreCase = true) }.distinct()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onComisionistaQueryChange(query: String) {
        _comisionistaQuery.value = query
    }

    fun onLocalidadQueryChange(query: String) {
        _localidadQuery.value = query
    }

    //New Destino
    fun insertDestino(destino: Destino) = viewModelScope.launch {
        insertDestinoUseCase(destino)
    }

}