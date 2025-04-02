package com.example.fletes.data.repositories

import com.example.fletes.data.model.CamionesRegistro
import com.example.fletes.data.model.CamionesRegistroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * A fake implementation of the [CamionesRegistroRepository] interface for testing purposes.
 *
 * This repository stores [CamionesRegistro] objects in an in-memory list, allowing for
 * quick and isolated testing without the need for a real database connection.
 *
 * This implementation is suitable for unit and integration tests where you want to:
 *  - Control the data that the repository returns.
 *  - Avoid dependencies on external resources (e.g., a real database).
 *  - Test the behavior of components that interact with the repository.
 *
 *  **Important:** The data is stored in memory and will be lost when the instance is destroyed.
 *
 */
class FakeCamionesRegistroRepository : CamionesRegistroRepository {
    private val data = mutableListOf<CamionesRegistro>()

    override fun getAllCamionesRegistrosStream(): Flow<List<CamionesRegistro>> = flow { emit(data.toList()) }

    override fun getCamionesRegistroStream(id: Int): Flow<CamionesRegistro?> = flow { emit(data.find { it.id == id }) }

    override suspend fun insertCamionesRegistro(camionesRegistro: CamionesRegistro) {
        val newId = data.maxOfOrNull { it.id.toInt() }?.plus(1) ?: 1
        data.add(camionesRegistro.copy(id = newId))
    }

    override suspend fun deleteCamionesRegistro(camionesRegistro: CamionesRegistro) {
        data.remove(camionesRegistro)
    }

    override suspend fun updateCamionesRegistro(camionesRegistro: CamionesRegistro) {
        val index = data.indexOfFirst { it.id == camionesRegistro.id }
        if (index != -1) {
            data[index] = camionesRegistro
        }
    }

    override fun getCamionesRegistroByCamionIdStream(camionId: Int): Flow<List<CamionesRegistro>> = flow {
        emit(data.filter { it.id == camionId })
    }
}