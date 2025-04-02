package com.example.fletes.data.repositories

import com.example.fletes.data.model.Camion
import com.example.fletes.data.model.CamionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCamionRepository: CamionRepository {
    private val data = mutableListOf<Camion>()

    override fun getAllCamionesStream(): Flow<List<Camion>> = flow { emit(data.toList()) }

    override fun getCamionStream(id: Int): Flow<Camion?> = flow { emit(data.find { it.id == id }) }

    override suspend fun insertCamion(camion: Camion) {
        val newId = data.maxOfOrNull { it.id }?.plus(1) ?: 1
        data.add(camion.copy(id = newId))
    }

    override suspend fun deleteCamion(camion: Camion) {
        data.remove(camion)
    }

    override suspend fun updateCamion(camion: Camion) {
        val index = data.indexOfFirst { it.id == camion.id }
        if (index != -1) {
            data[index] = camion
        }
    }
}