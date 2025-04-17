package com.example.fletes.data.repositories.implementations

import com.example.fletes.data.repositories.interfaces.DestinationRepositoryInterface
import com.example.fletes.data.room.Destino
import com.example.fletes.data.room.interfacesDao.DestinationDao
import kotlinx.coroutines.flow.Flow

class DestinationRepositoryImpl(private val destinationsDao: DestinationDao) : DestinationRepositoryInterface {
    override suspend fun insertDestino(destino: Destino) {
        return destinationsDao.insertDestino(destino)
    }

    override suspend fun deleteDestino(destino: Destino) {
        return destinationsDao.deleteDestino(destino)
    }

    override suspend fun updateDestino(destino: Destino) {
        return destinationsDao.updateDestino(destino)
    }

    override fun getDestinoStream(id: Int): Flow<Destino?> {
        return destinationsDao.getDestinoById(id)
    }

    override fun getActiveDestinosStream(): Flow<List<Destino>> {
        return destinationsDao.getActiveDestinos()
    }

    override fun getActiveDestinosCountStream(): Flow<Int> {
        return destinationsDao.getActiveDestinosCount()
    }

    override fun getAllDestinosStream(): Flow<List<Destino>> {
        return destinationsDao.getAllDestinos()
    }

    override fun searchComisionista(query: String): Flow<List<String>> {
        return destinationsDao.searchComisionista(query)
    }

    override fun searchLocalidad(query: String): Flow<List<String>> {
        return destinationsDao.searchLocalidad(query)
    }
}