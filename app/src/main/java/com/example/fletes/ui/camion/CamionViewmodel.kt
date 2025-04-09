package com.example.fletes.ui.camion

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fletes.data.repositories.CamionRepository
import com.example.fletes.data.room.Camion
import com.example.fletes.domain.DniValidator
import com.example.fletes.domain.PatenteValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class CamionUiState(
    val choferName: String = "",
    val choferDni: String = "",
    val driverDniErrorMessage: String? = "Insert Dni",
    val isValidDni: Boolean = true,
    val patenteTractor: String = "",
    val patenteTractorErrorMessage: String? = null,
    val isValidPatenteTractor: Boolean = true,
    val patenteJaula: String = "",
    val patenteJaulaErrorMessage: String? = null,
    val isValidPatenteJaula: Boolean = true,
    val kmService: Int = 20000,
    val showDialog: Boolean = false
)


class CamionViewModel(
    private val camionRepository: CamionRepository,
    private val dniValidator: DniValidator,
    private val licenseStringValidatorResult: PatenteValidator
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
        val validationResult = dniValidator.validateDni(newValue)
        Log.d("CamionViewModel", "Validation result: $validationResult")
        _uiState.update { it.copy(
            choferDni = newValue,
            driverDniErrorMessage = validationResult.errorMessage,
            isValidDni = validationResult.isValid
        ) }

    }

    fun onPatenteTractorValueChange(newValue: String) {
        val validatorResult = licenseStringValidatorResult.validatePatente(newValue)
        _uiState.update {
            it.copy(
                patenteTractor = newValue,
                patenteTractorErrorMessage = validatorResult.errorMessage,
                isValidPatenteTractor = validatorResult.isValid
            )
        }
    }

    fun onPatenteJaulaValueChange(newValue: String) {
        val validatorResult = licenseStringValidatorResult.validatePatente(newValue)
        _uiState.update {
            it.copy(
                patenteJaula = newValue,
                patenteJaulaErrorMessage = validatorResult.errorMessage,
                isValidPatenteJaula = validatorResult.isValid
            )
        }
    }

    fun insertCamion() {
        viewModelScope.launch {
            val camionToInsert = Camion(
                createdAt = LocalDate.now(),
                choferName = _uiState.value.choferName,
                choferDni = _uiState.value.choferDni.toInt(),
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