package com.example.fletes.data.repositories.implementations

import com.example.fletes.data.repositories.interfaces.TruckRepositoryInterface
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.interfacesDao.TruckDao
import kotlinx.coroutines.flow.Flow

class TruckRepositoryImpl(private val truckDao: TruckDao): TruckRepositoryInterface {


    override suspend fun insertCamion(camion: Camion) {
        return truckDao.insertTruck(camion)
    }

    override fun getActiveTrucks(): Flow<List<Camion>> {
        return truckDao.getActiveTrucks()
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

    override suspend fun updateTruckIsActive(camion: Camion) {
        return truckDao.updateTruckIsActive(camion.id, camion.isActive)
    }
}