package com.example.fletes.data.repositories

import com.example.fletes.data.room.AppDao
import com.example.fletes.data.room.Camion
import kotlinx.coroutines.flow.Flow

class CamionRepositoryImpl(private val appDao: AppDao): CamionRepository {
    override suspend fun insertCamion(camion: Camion) {
        return appDao.insertCamion(camion)
    }

    override suspend fun deleteCamion(camion: Camion) {
        return appDao.deleteCamion(camion)
    }

    override suspend fun deleteAlllCamiones() {
        return appDao.deleteAlllCamiones()
    }

    override suspend fun getCamionById(id: Int): Camion? {
        return appDao.getCamionById(id)
    }

    override fun getAllCamiones(): Flow<List<Camion>> {
       return appDao.getAllCamiones()
    }
}