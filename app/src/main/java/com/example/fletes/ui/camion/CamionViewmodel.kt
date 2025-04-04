package com.example.fletes.ui.camion

import androidx.lifecycle.ViewModel
import com.example.fletes.data.repositories.CamionRepository
import com.example.fletes.data.room.Camion
import kotlinx.coroutines.flow.Flow

class CamionViewModel(private val camionRepository: CamionRepository): ViewModel() {
    val camiones: Flow<List<Camion>> = camionRepository.getAllCamiones()
    suspend fun insertCamion(camion: Camion) {
        camionRepository.insertCamion(camion)
    }
}