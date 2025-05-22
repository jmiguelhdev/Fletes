package com.example.fletes.ui.screenTruck

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fletes.data.repositories.interfaces.TruckRepositoryInterface
import com.example.fletes.data.room.Camion
import com.example.fletes.domain.validators.DniValidator
import com.example.fletes.domain.validators.PatenteValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed class DialogState {
    object None : DialogState()
    object Insert : DialogState()
    object Edit : DialogState()
}

data class TruckUiState(
    val choferName: String = "",
    val choferDni: String = "",
    val driverDniErrorMessage: String? = null,
    val isValidDni: Boolean = true, // Based on DniValidator.validateDni("") currently returning isValid = true
    val patenteTractor: String = "",
    val patenteTractorErrorMessage: String? = null,
    val isValidPatenteTractor: Boolean = true,
    val patenteJaula: String = "",
    val patenteJaulaErrorMessage: String? = null,
    val isValidPatenteJaula: Boolean = true,
    // val kmService: Int = 20000, // Removed as it's unused
    val currentDialog: DialogState = DialogState.None,
    val showSnackbar: Boolean = false,
    val snackbarMessage: String = "",
    val editingTruckId: Int? = null
)


class TruckViewModel(
    private val camionRepository: TruckRepositoryInterface,
    private val dniValidator: DniValidator,
    private val licenseStringValidatorResult: PatenteValidator
) : ViewModel() {
    private val _camiones = MutableStateFlow<List<Camion>>(emptyList())
    val camiones: StateFlow<List<Camion>> = _camiones.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    private val _uiState = MutableStateFlow(TruckUiState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TruckUiState()
    )

    // Function to load the list of camiones
    fun loadCamiones() {
        viewModelScope.launch {
            try {
                // Update the list of camiones in the state flow
                camionRepository.getAllCamiones()
                    .collectLatest { camiones ->
                        _camiones.value = camiones
                    }
            } catch (e: Exception) {
                // Handle the error
                Log.e("TruckViewModel", "Error loading camiones", e)
                _uiState.update {
                    it.copy(
                        showSnackbar = true,
                        snackbarMessage = "Error loading trucks. Please try again."
                    )
                }
            }
        }
    }


    fun showDialog() {
        // This function is used to show the insert dialog
        _uiState.update { it.copy(currentDialog = DialogState.Insert) }
    }

    fun snackbarShown() {
        _uiState.update {
            it.copy(showSnackbar = false, snackbarMessage = "")
        }
    }

    fun hideDialog() {
        _uiState.update {
            it.copy(
                currentDialog = DialogState.None,
                editingTruckId = null,
                choferName = "",
                choferDni = "",
                patenteTractor = "",
                patenteJaula = ""
            )
        }
    }

    fun onChoferNameValueChange(newValue: String) {
        Log.d("CamionViewModel", "Chofer name changed to $newValue")
        _uiState.update { it.copy(choferName = newValue) }
    }

    fun onChoferDniValueChange(newValue: String) {
        val validationResult = dniValidator.validateDni(newValue)
        Log.d("CamionViewModel", "Validation result: $validationResult")
        _uiState.update {
            it.copy(
                choferDni = newValue,
                driverDniErrorMessage = validationResult.errorMessage,
                isValidDni = validationResult.isValid
            )
        }

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
            )
            camionRepository.insertCamion(camionToInsert)
            Log.d("CamionViewModel", "Camion inserted $camionToInsert")
        }
        // hideDialog() will set currentDialog = DialogState.None and clear fields
        hideDialog()
        _uiState.update {
            it.copy(
                showSnackbar = true,
                snackbarMessage = "Camión insertado correctamente"
            )
        }
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

    fun onShowEditDialog(id: Int) {
        viewModelScope.launch {
            val camionToUpdate = camionRepository.getCamionById(id)
            if (camionToUpdate != null) {
                _uiState.update {
                    it.copy(
                        currentDialog = DialogState.Edit,
                        editingTruckId = id,
                        choferName = camionToUpdate.choferName,
                        choferDni = camionToUpdate.choferDni.toString(),
                        patenteTractor = camionToUpdate.patenteTractor,
                        patenteJaula = camionToUpdate.patenteJaula,
                    )
                }
            }
        }
    }

    fun updateCamion() {
        val currentEditingId = _uiState.value.editingTruckId
        if (currentEditingId == null) {
            Log.e("TruckViewModel", "editingTruckId is null, cannot update.")
            // hideDialog() will set currentDialog = DialogState.None
            hideDialog()
            _uiState.update {
                it.copy(
                    showSnackbar = true,
                    snackbarMessage = "Error: No se pudo actualizar el camión."
                )
            }
            return
        }

        viewModelScope.launch {
            val camionToUpdate = camionRepository.getCamionById(currentEditingId)
            Log.d("CamionViewModel", "name ${_uiState.value.choferName}")
            if (camionToUpdate != null) {
                val updatedCamion = camionToUpdate.copy(
                    choferName = _uiState.value.choferName,
                    choferDni = _uiState.value.choferDni.toInt(),
                    patenteTractor = _uiState.value.patenteTractor,
                    patenteJaula = _uiState.value.patenteJaula,
                )
                camionRepository.updateCamion(updatedCamion)
                Log.d("CamionViewModel", "Camion updated $updatedCamion")
                // hideDialog() will set currentDialog = DialogState.None and clear fields
                hideDialog()
                _uiState.update {
                    it.copy(
                        showSnackbar = true,
                        snackbarMessage = "Camión actualizado correctamente"
                    )
                }
            } else {
                Log.e("TruckViewModel", "Camion with id $currentEditingId not found for update.")
                // hideDialog() will set currentDialog = DialogState.None
                hideDialog()
                _uiState.update {
                    it.copy(
                        showSnackbar = true,
                        snackbarMessage = "Error: Camión no encontrado."
                    )
                }
            }
        }
    }

    fun deactivateTruck(camion: Camion) {
        viewModelScope.launch {
            val updatedCamion = camion.copy(
                isActive = false
            )
            try {
                camionRepository.updateTruckIsActive(updatedCamion)
                Log.d("TruckViewModel", "Camion deactivated $updatedCamion")
            } catch (e: Exception) {
                Log.e("TruckViewModel", "Error deactivating camion", e)
            }
        }
    }
    fun activateTruck(camion: Camion) {
        viewModelScope.launch {
            val updatedCamion = camion.copy(
                isActive = true
            )
            try {
                camionRepository.updateTruckIsActive(updatedCamion)
                Log.d("TruckViewModel", "Camion activated $updatedCamion")
            } catch (e: Exception) {
                Log.e("TruckViewModel", "Error activating camion", e)
            }
        }
    }

}