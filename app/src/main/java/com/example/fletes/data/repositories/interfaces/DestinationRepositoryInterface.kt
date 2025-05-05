package com.example.fletes.data.repositories.interfaces

import com.example.fletes.data.room.Destino
import kotlinx.coroutines.flow.Flow

interface DestinationRepositoryInterface {
    suspend fun insertDestino(destino: Destino)
    suspend fun deleteDestino(destino: Destino)
    suspend fun updateDestino(destino: Destino)
    fun getDestinoStream(id: Int): Flow<Destino?>
    fun getActiveDestinosStream(): Flow<List<Destino>>
    fun getUnActiveDestinosStream(): Flow<List<Destino>>
    fun getActiveDestinosCountStream(): Flow<Int>
    fun getAllDestinosStream(): Flow<List<Destino>>
    fun searchComisionista(query: String): Flow<List<String>>
    fun searchLocalidad(query: String): Flow<List<String>>
}