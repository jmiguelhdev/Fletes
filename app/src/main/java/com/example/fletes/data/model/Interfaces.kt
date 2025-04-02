package com.example.fletes.data.model

import kotlinx.coroutines.flow.Flow

//Interfaces
interface CamionRepository {
    fun getAllCamionesStream(): Flow<List<Camion>>
    fun getCamionStream(id: Int): Flow<Camion?>
    suspend fun insertCamion(camion: Camion)
    suspend fun deleteCamion(camion: Camion)
    suspend fun updateCamion(camion: Camion)
}

interface DestinoRepository {
    fun getAllDestinosStream(): Flow<List<Destino>>
    fun getDestinoStream(id: Int): Flow<Destino?>
    suspend fun insertDestino(destino: Destino)
    suspend fun deleteDestino(destino: Destino)
    suspend fun updateDestino(destino: Destino)
}

interface CamionesRegistroRepository {
    fun getAllCamionesRegistrosStream(): Flow<List<CamionesRegistro>>
    fun getCamionesRegistroStream(id: Int): Flow<CamionesRegistro?>
    suspend fun insertCamionesRegistro(camionesRegistro: CamionesRegistro)
    suspend fun deleteCamionesRegistro(camionesRegistro: CamionesRegistro)
    suspend fun updateCamionesRegistro(camionesRegistro: CamionesRegistro)
    fun getCamionesRegistroByCamionIdStream(camionId: Int): Flow<List<CamionesRegistro>>
}