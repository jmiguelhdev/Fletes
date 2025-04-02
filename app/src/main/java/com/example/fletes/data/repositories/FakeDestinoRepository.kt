package com.example.fletes.data.repositories

import androidx.compose.animation.core.copy
import androidx.compose.ui.unit.plus
import com.example.fletes.data.model.Destino
import com.example.fletes.data.model.DestinoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDestinoRepository : DestinoRepository {

    private val data = mutableListOf<Destino>()

    override fun getAllDestinosStream(): Flow<List<Destino>> = flow { emit(data.toList()) }

    override fun getDestinoStream(id: Int): Flow<Destino?> = flow { emit(data.find { it.id == id }) }

    override suspend fun insertDestino(destino: Destino) {
        val newId = data.maxOfOrNull { it.id }?.plus(1) ?: 1
        data.add(destino.copy(id = newId))
    }

    override suspend fun deleteDestino(destino: Destino) {
        data.remove(destino)
    }

    override suspend fun updateDestino(destino: Destino) {
        val index = data.indexOfFirst { it.id == destino.id }
        if (index != -1) {
            data[index] = destino
        }
    }
}