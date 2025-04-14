package com.example.fletes.data.repositories

import CamionesRegistroRepository
import com.example.fletes.data.room.AppDao
import com.example.fletes.data.room.CamionesRegistro
import kotlinx.coroutines.flow.Flow

class TrucksRepoImp(private val appDao: AppDao) : CamionesRegistroRepository{
    override suspend fun insertCamionesRegistro(camionesRegistro: CamionesRegistro) {
        return appDao.insertCamionesRegistro(camionesRegistro)
    }

    override suspend fun deleteCamionesRegistro(camionesRegistro: CamionesRegistro) {
        return appDao.updateCamionRegistro(camionesRegistro)
    }

    override suspend fun updateCamionesRegistro(camionesRegistro: CamionesRegistro) {
        return appDao.updateCamionRegistro(camionesRegistro)
    }

    override fun getAllCamionesRegistrosStream(): Flow<List<CamionesRegistro>> {
        return appDao.getAllCamionesRegistros()
    }

    override fun getCamionesRegistroStream(id: Int): Flow<CamionesRegistro?> {
        return appDao.getCamionesRegistroById(id)
    }

    override fun getLastTripByCamionId(camionId: Int): Flow<CamionesRegistro?> {
        return appDao.getLastTripByCamionId(camionId)
    }

    override fun getCamionesRegistroByCamionIdStream(camionId: Int): Flow<List<CamionesRegistro>> {
       return appDao.getRegistrationsForTruck(camionId)
    }
}