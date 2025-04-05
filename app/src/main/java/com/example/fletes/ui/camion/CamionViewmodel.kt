package com.example.fletes.ui.camion

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fletes.data.repositories.CamionRepository
import com.example.fletes.data.room.Camion
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class CamionViewModel(private val camionRepository: CamionRepository): ViewModel() {
    val camiones = camionRepository.getAllCamiones().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    fun insertCamion() {
        viewModelScope.launch {
            val camionToInsert = Camion(
                createdAt = LocalDate.now(),
                choferName = "John Doe",
                choferPrice = 100.0,
                patenteTractor = "AA123BB",
                patenteJaula = "CC456DD",
                kmService = 10000
            )
            camionRepository.insertCamion(camionToInsert)
            Log.d("CamionViewModel", "Camion inserted $camionToInsert")
        }
    }

    fun deleteAllCamiones() {
        viewModelScope.launch {
            camionRepository.deleteAlllCamiones()
            Log.d("CamionViewModel", "All camiones deleted")
        }
    }
}