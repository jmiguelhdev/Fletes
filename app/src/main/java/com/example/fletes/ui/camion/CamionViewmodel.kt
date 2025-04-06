package com.example.fletes.ui.camion

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fletes.data.repositories.CamionRepository
import com.example.fletes.data.room.Camion
import com.example.fletes.domain.DniValidationResult
import com.example.fletes.domain.DniValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class CamionUiState(
    val choferName: String = "",
    val choferDni: Int? = 0,
    val choferDniError: String? = "",
    val patenteTractor: String = "",
    val patenteJaula: String = "",
    val kmService: Int = 20000,
    val showDialog: Boolean = false
)


class CamionViewModel(
    private val camionRepository: CamionRepository,
    private val dniValidator: DniValidator
) : ViewModel() {
    val camiones = camionRepository.getAllCamiones().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    private val _uiState = MutableStateFlow(CamionUiState())
    val uiState = _uiState.asStateFlow()

    fun showDialog() {
        _uiState.update { it.copy(showDialog = true) }
    }

    fun hideDialog() {
        _uiState.update { it.copy(showDialog = false) }
    }

    fun onChoferNameValueChange(newValue: String) {
        _uiState.update { it.copy(choferName = newValue) }
    }

    fun onChoferDniValueChange(newValue: String) {
        val validationResult: DniValidationResult = dniValidator.validateDni(newValue)
        _uiState.update {
            it.copy(
                choferDni = validationResult.dni,
                choferDniError = validationResult.error
            )
        }
    }

    fun onPatenteTractorValueChange(newValue: String) {
        val uppercaseValue = newValue.uppercase()
        val formattedValue = if (uppercaseValue.length <= 6 && uppercaseValue.length > 2) {
            uppercaseValue.substring(
                0,
                2
            ) + if (uppercaseValue.length >= 3) uppercaseValue.substring(2)
                .filter { it.isDigit() } else ""
        } else uppercaseValue
        val validValue = formattedValue.matches(Regex("[A-Z]{2}[0-9]{3}[A-Z]{2}|[A-Z]{3}[0-9]{3}"))
        _uiState.update {
            it.copy(
                patenteTractor = newValue//if (validValue) formattedValue else _uiState.value.patenteTractor
            )
        }
    }

    fun onPatenteJaulaValueChange(newValue: String) {
        val uppercaseValue = newValue.uppercase()
        val formattedValue = if (uppercaseValue.length <= 6 && uppercaseValue.length > 2) {
            uppercaseValue.substring(
                0,
                2
            ) + if (uppercaseValue.length >= 3) uppercaseValue.substring(2)
                .filter { it.isDigit() } else ""
        } else uppercaseValue
        val validValue = formattedValue.matches(Regex("[A-Z]{2}[0-9]{3}[A-Z]{2}|[A-Z]{3}[0-9]{3}"))
        _uiState.value = _uiState.value.copy(
            patenteJaula = if (validValue) formattedValue else _uiState.value.patenteJaula
        )
    }

    fun insertCamion() {
        viewModelScope.launch {
            val camionToInsert = Camion(
                createdAt = LocalDate.now(),
                choferName = _uiState.value.choferName,
                choferDni = _uiState.value.choferDni!!,
                patenteTractor = _uiState.value.patenteTractor,
                patenteJaula = _uiState.value.patenteJaula,
                kmService = 20000
            )
            camionRepository.insertCamion(camionToInsert)
            Log.d("CamionViewModel", "Camion inserted $camionToInsert")
        }
        hideDialog()
    }

    fun deleteAllCamiones() {
        viewModelScope.launch {
            camionRepository.deleteAlllCamiones()
            Log.d("CamionViewModel", "All camiones deleted")
        }
    }

    fun deleteCamion(id: Int) {
        viewModelScope.launch {
            val camionToDelete = camionRepository.getCamionById(id)
            if (camionToDelete != null) {
                camionRepository.deleteCamion(camionToDelete)
                Log.d("CamionViewModel", "Camion deleted $camionToDelete")
            }
        }
    }
}