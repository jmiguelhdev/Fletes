package com.example.fletes.ui.screenTrucksJourney

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fletes.data.model.DecimalTextFieldData
import com.example.fletes.data.model.truckJourneyData.TruckJourneyData
import com.example.fletes.data.room.Camion
import com.example.fletes.domain.GetAllJourneyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
    //creo que no a aqui, creare otro viewModel para el trucks detail
    val truckSelected: Camion = Camion(
        choferName = "",
        choferDni = 0,
        patenteTractor = "",
        patenteJaula = "",
        isActive = true
    )
)


class TruckJourneyViewModel(
    private val savedStateHandle: SavedStateHandle,
    getAllJourneyUseCase: GetAllJourneyUseCase
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

    val allJourneys = getAllJourneyUseCase().stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}