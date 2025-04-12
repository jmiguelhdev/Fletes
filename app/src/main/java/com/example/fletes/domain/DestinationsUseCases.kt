package com.example.fletes.domain



import DestinoRepository
import com.example.fletes.data.room.Destino
import kotlinx.coroutines.flow.Flow

class SearchComisionistaUseCase(private val repository: DestinoRepository) {
    operator fun invoke(query: String): Flow<List<String>> {
        return repository.searchComisionista(query)
    }
}

class SearchLocalidadUseCase(private val repository: DestinoRepository) {
    operator fun invoke(query: String): Flow<List<String>> {
        return repository.searchLocalidad(query)
    }
}

class GetAllDestinosUseCase(private val repository: DestinoRepository){
    operator fun invoke(): Flow<List<Destino>> {
        return repository.getAllDestinosStream()
    }
}
class InsertDestinoUseCase(private val repository: DestinoRepository){
    suspend operator fun invoke(destino: Destino){
        repository.insertDestino(destino)
    }
}