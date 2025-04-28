package com.example.fletes.data.repositories.interfaces

import com.example.fletes.data.room.Camion
import kotlinx.coroutines.flow.Flow

interface TruckRepositoryInterface {
    suspend fun insertCamion(camion: Camion)
    suspend fun deleteCamion(camion: Camion)
    suspend fun deleteAlllCamiones()
    suspend fun updateCamion(camion: Camion)
    suspend fun getCamionById(id: Int): Camion?
    fun getAllCamiones(): Flow<List<Camion>>
    suspend fun updateTruckIsActive(camion: Camion)
}