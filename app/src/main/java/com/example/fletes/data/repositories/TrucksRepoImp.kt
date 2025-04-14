package com.example.fletes.data.repositories

import CamionesRegistroRepository
import com.example.fletes.data.room.CamionesRegistro
import com.example.fletes.data.room.TrucksRegistrationDao
import kotlinx.coroutines.flow.Flow

class TrucksRepoImp(private val trucksRegistrationDao: TrucksRegistrationDao) : CamionesRegistroRepository{
    override suspend fun insertCamionesRegistro(camionesRegistro: CamionesRegistro) {
        return trucksRegistrationDao.insertCamionesRegistro(camionesRegistro)
    }

    override suspend fun deleteCamionesRegistro(camionesRegistro: CamionesRegistro) {
        return trucksRegistrationDao.updateCamionRegistro(camionesRegistro)
    }

    override suspend fun updateCamionesRegistro(camionesRegistro: CamionesRegistro) {
        return trucksRegistrationDao.updateCamionRegistro(camionesRegistro)
    }

    override fun getAllCamionesRegistrosStream(): Flow<List<CamionesRegistro>> {
        return trucksRegistrationDao.getAllCamionesRegistros()
    }

    override fun getCamionesRegistroStream(id: Int): Flow<CamionesRegistro?> {
        return trucksRegistrationDao.getCamionesRegistroById(id)
    }

    override fun getLastTripByCamionId(camionId: Int): Flow<CamionesRegistro?> {
        return trucksRegistrationDao.getLastTripByCamionId(camionId)
    }

    override fun getCamionesRegistroByCamionIdStream(camionId: Int): Flow<List<CamionesRegistro>> {
       return trucksRegistrationDao.getRegistrationsForTruck(camionId)
    }
}