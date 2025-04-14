package com.example.fletes.data.repositories

import DestinoRepository
import com.example.fletes.data.room.DestinationDao
import com.example.fletes.data.room.Destino
import kotlinx.coroutines.flow.Flow

class DestinationsRepoImp(private val destinationsDao: DestinationDao) : DestinoRepository {
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