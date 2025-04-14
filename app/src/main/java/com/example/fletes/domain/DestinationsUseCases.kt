package com.example.fletes.domain



import com.example.fletes.data.repositories.implementations.DestinationRepositoryImpl
import com.example.fletes.data.room.Destino
import kotlinx.coroutines.flow.Flow

class SearchComisionistaUseCase(private val repository: DestinationRepositoryImpl) {
    operator fun invoke(query: String): Flow<List<String>> {
        return repository.searchComisionista(query)
    }
}

class SearchLocalidadUseCase(private val repository: DestinationRepositoryImpl) {
    operator fun invoke(query: String): Flow<List<String>> {
        return repository.searchLocalidad(query)
    }
}

class GetAllDestinosUseCase(private val repository: DestinationRepositoryImpl){
    operator fun invoke(): Flow<List<Destino>> {
        return repository.getAllDestinosStream()
    }
}
class InsertDestinoUseCase(private val repository: DestinationRepositoryImpl){
    suspend operator fun invoke(destino: Destino){
        repository.insertDestino(destino)
    }
}