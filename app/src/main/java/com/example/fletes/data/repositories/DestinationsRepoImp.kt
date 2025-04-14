package com.example.fletes.data.repositories

import DestinoRepository
import com.example.fletes.data.room.AppDao
import com.example.fletes.data.room.Destino
import kotlinx.coroutines.flow.Flow

class DestinationsRepoImp(private val appDao: AppDao) : DestinoRepository {
    override suspend fun insertDestino(destino: Destino) {
        return appDao.insertDestino(destino)
    }

    override suspend fun deleteDestino(destino: Destino) {
        return appDao.deleteDestino(destino)
    }

    override suspend fun updateDestino(destino: Destino) {
        return appDao.updateDestino(destino)
    }

    override fun getDestinoStream(id: Int): Flow<Destino?> {
        return appDao.getDestinoById(id)
    }

    override fun getAllDestinosStream(): Flow<List<Destino>> {
        return appDao.getAllDestinos()
    }

    override fun searchComisionista(query: String): Flow<List<String>> {
        return appDao.searchComisionista(query)
    }

    override fun searchLocalidad(query: String): Flow<List<String>> {
        return appDao.searchLocalidad(query)
    }
}