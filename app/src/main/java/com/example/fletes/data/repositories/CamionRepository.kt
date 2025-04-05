package com.example.fletes.data.repositories

import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.CamionesRegistro
import com.example.fletes.data.room.Destino
import kotlinx.coroutines.flow.Flow

interface CamionRepository {
    // Camion
    suspend fun insertCamion(camion: Camion)
    suspend fun deleteCamion(camion: Camion)
    suspend fun deleteAlllCamiones()
    suspend fun getCamionById(id: Int): Camion?
    fun getAllCamiones(): Flow<List<Camion>>
}

interface DestinoRepository {
    // Destino
    suspend fun insertDestino(destino: Destino)
    suspend fun getDestinoById(id: Int): Destino?
    fun getAllDestinos(): Flow<List<Destino>>
}

interface CamionesRegistroRepository {
    // CamionesRegistro
    suspend fun insertCamionesRegistro(camionesRegistro: CamionesRegistro)
    suspend fun getCamionesRegistroById(id: Int): CamionesRegistro?
    fun getAllCamionesRegistros(): Flow<List<CamionesRegistro>>
    fun getRegistrationsForTruck(camionId: Int): Flow<List<CamionesRegistro>>
    fun getRegistrationsForDestination(destinoId: Int): Flow<List<CamionesRegistro>>
    fun getAllRegistrations():Flow<List<CamionesRegistro>>
}