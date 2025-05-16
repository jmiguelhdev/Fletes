package com.example.fletes.data.repositories.interfaces

import com.example.fletes.data.room.CamionesRegistro
import kotlinx.coroutines.flow.Flow

interface TrucksJourneyRepositoryInterface {
    suspend fun insertCamionesRegistro(camionesRegistro: CamionesRegistro)
    suspend fun deleteCamionesRegistro(camionesRegistro: CamionesRegistro)
    suspend fun updateCamionesRegistro(camionesRegistro: CamionesRegistro)
    fun getAllCamionesRegistrosStream(): Flow<List<CamionesRegistro>>
    suspend fun getCamionesRegistroStream(id: Int): Flow<CamionesRegistro?>
    fun getLastTripByCamionId(camionId: Int): Flow<CamionesRegistro?>
    fun getCamionesRegistroByCamionIdStream(camionId: Int): Flow<List<CamionesRegistro>>
}