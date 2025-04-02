package com.example.fletes.data.repositories

import com.example.fletes.data.model.Camion
import com.example.fletes.data.model.CamionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * A fake implementation of the [CamionRepository] interface for testing purposes.
 *
 * This class simulates a database or remote data source for [Camion] entities.
 * It stores data in an in-memory list (`data`) and provides methods to interact with it.
 * It's designed to be used in tests where a real database interaction is not desired.
 */
class FakeCamionRepository: CamionRepository {
    private val data = mutableListOf<Camion>()

    override fun getAllCamionesStream(): Flow<List<Camion>> = flow { emit(data.toList()) }

    override fun getCamionStream(id: Int): Flow<Camion?> = flow { emit(data.find { it.id == id }) }

    override suspend fun insertCamion(camion: Camion) {
        val newId = data.maxOfOrNull { it.id!!.toInt() }?.plus(1) ?: 1
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