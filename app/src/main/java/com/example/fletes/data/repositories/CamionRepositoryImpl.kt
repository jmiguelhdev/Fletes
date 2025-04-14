package com.example.fletes.data.repositories

import CamionRepository
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.TruckDAo
import kotlinx.coroutines.flow.Flow

class CamionRepositoryImpl(private val truckDao: TruckDAo): CamionRepository {


    override suspend fun insertCamion(camion: Camion) {
        return truckDao.insertTruck(camion)
    }

    override suspend fun deleteCamion(camion: Camion) {
        return truckDao.deleteCamion(camion)
    }

    override suspend fun deleteAlllCamiones() {
        return truckDao.deleteAllTrucks()
    }

    override suspend fun updateCamion(camion: Camion) {
        return truckDao.updateTruck(camion)
    }

    override suspend fun getCamionById(id: Int): Camion? {
        return truckDao.getTruckById(id)
    }

    override fun getAllCamiones(): Flow<List<Camion>> {
       return truckDao.getAllTrucks()
    }
}