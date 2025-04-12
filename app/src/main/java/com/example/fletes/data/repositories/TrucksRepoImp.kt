package com.example.fletes.data.repositories

import com.example.fletes.data.room.AppDao
import com.example.fletes.data.room.CamionesRegistro
import kotlinx.coroutines.flow.Flow

class TrucksRepoImp(private val appDao: AppDao) : CamionesRegistroRepository{
    override suspend fun insertCamionesRegistro(camionesRegistro: CamionesRegistro) {
        return appDao.insertCamionesRegistro(camionesRegistro)
    }

    override suspend fun getCamionesRegistroById(id: Int): CamionesRegistro? {
        return appDao.getCamionesRegistroById(id)
    }

    override fun getAllCamionesRegistros(): Flow<List<CamionesRegistro>> {
        return appDao.getAllCamionesRegistros()
    }

    override fun getRegistrationsForTruck(camionId: Int): Flow<List<CamionesRegistro>> {
        return appDao.getRegistrationsForTruck(camionId)
    }

    override fun getRegistrationsForDestination(destinoId: Int): Flow<List<CamionesRegistro>> {
        return appDao.getRegistrationsForDestination(destinoId)
    }

    override fun getAllRegistrations(): Flow<List<CamionesRegistro>> {
        return appDao.getAllRegistrations()
    }

    override suspend fun updateCamionRegistro(camionesRegistro: CamionesRegistro) {
        return appDao.updateCamionRegistro(camionesRegistro)
    }

    override fun getLastTripByCamionId(id: Int): CamionesRegistro {
        return appDao.getLastTripByCamionId(id)
    }
}