package com.example.fletes.data.repositories

import com.example.fletes.data.model.CamionesRegistro
import com.example.fletes.data.model.CamionesRegistroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCamionesRegistroRepository : CamionesRegistroRepository {
    private val data = mutableListOf<CamionesRegistro>()

    override fun getAllCamionesRegistrosStream(): Flow<List<CamionesRegistro>> = flow { emit(data.toList()) }

    override fun getCamionesRegistroStream(id: Int): Flow<CamionesRegistro?> = flow { emit(data.find { it.id == id }) }

    override suspend fun insertCamionesRegistro(camionesRegistro: CamionesRegistro) {
        val newId = data.maxOfOrNull { it.id }?.plus(1) ?: 1
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