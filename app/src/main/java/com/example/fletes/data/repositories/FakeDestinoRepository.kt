package com.example.fletes.data.repositories

import androidx.compose.animation.core.copy
import androidx.compose.ui.unit.plus
import com.example.fletes.data.model.Destino
import com.example.fletes.data.model.DestinoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * A fake implementation of the [DestinoRepository] interface for testing purposes.
 *
 * This class simulates a repository for managing [Destino] objects in memory.
 * It allows for insertion, deletion, updating, and retrieval of destinos without
 * interacting with a real database. All operations are performed on an in-memory list.
 */
class FakeDestinoRepository : DestinoRepository {

    private val data = mutableListOf<Destino>()
    // Use an atomic integer to ensure thread safety for ID generation
    private var nextId = java.util.concurrent.atomic.AtomicInteger(1)

    override fun getAllDestinosStream(): Flow<List<Destino>> = flow {
        //Emit a copy to avoid issues if the original data list is changed later
        emit(data.toList())
    }

    override fun getDestinoStream(id: Int): Flow<Destino?> = flow {
        //emit a copy to avoid issues if the original data list is changed later
        emit(data.find { it.id == id }?.copy())
    }

    override suspend fun insertDestino(destino: Destino) {
        // Use AtomicInteger for thread-safe ID generation
        val newId = nextId.getAndIncrement()
        data.add(destino.copy(id = newId))
    }

    override suspend fun deleteDestino(destino: Destino) {
        // Remove based on ID for robustness
        data.removeAll { it.id == destino.id }
    }

    override suspend fun updateDestino(destino: Destino) {
        val index = data.indexOfFirst { it.id == destino.id }
        if (index != -1) {
            // Update with a copy to avoid potential side effects
            data[index] = destino.copy()
        }
    }

    // Helper function for testing
    fun clearData(){
        data.clear()
        nextId.set(1)
    }
}

