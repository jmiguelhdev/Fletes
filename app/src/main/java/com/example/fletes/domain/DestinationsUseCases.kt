package com.example.fletes.domain



import com.example.fletes.data.repositories.implementations.TruckRepositoryImpl
import com.example.fletes.data.repositories.interfaces.DestinationRepositoryInterface
import com.example.fletes.data.repositories.interfaces.TruckRepositoryInterface
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.Destino
import kotlinx.coroutines.flow.Flow

class GetActiveDestinosUseCase(private val repository: DestinationRepositoryInterface) {
    operator fun invoke(): Flow<List<Destino>> {
        return repository.getActiveDestinosStream()
    }
}

class GetActiveDispatchCount(private val repository: DestinationRepositoryInterface) {
    operator fun invoke(): Flow<Int> {
        return repository.getActiveDestinosCountStream()
    }
}

class SearchComisionistaUseCase(private val repository: DestinationRepositoryInterface) {
    operator fun invoke(query: String): Flow<List<String>> {
        return repository.searchComisionista(query)
    }
}

class SearchLocalidadUseCase(private val repository: DestinationRepositoryInterface) {
    operator fun invoke(query: String): Flow<List<String>> {
        return repository.searchLocalidad(query)
    }
}

class GetAllDestinosUseCase(private val repository: DestinationRepositoryInterface){
    operator fun invoke(): Flow<List<Destino>> {
        return repository.getAllDestinosStream()
    }
}
class InsertDestinoUseCase(private val repository: DestinationRepositoryInterface){
    suspend operator fun invoke(destino: Destino){
        repository.insertDestino(destino)
    }
}

class DeleteDestinoUseCase(private val repository: DestinationRepositoryInterface){
    suspend operator fun invoke(destino: Destino){
        repository.deleteDestino(destino)
    }
}

class UpdateDestinoUseCase(private val repository: DestinationRepositoryInterface){
    suspend operator fun invoke(destino: Destino){
        repository.updateDestino(destino)
    }

}

class GetAllTrucks(private val repository: TruckRepositoryInterface){
    operator fun invoke(): Flow<List<Camion>> {
        return repository.getAllCamiones()
    }
}

class GetActiveTrucks(private val repository: TruckRepositoryInterface){
     operator fun invoke(): Flow<List<Camion>> {
        return repository.getActiveTrucks()
    }
}

class UpdateTruckIsActiveUseCase(private val repository: TruckRepositoryInterface){
    suspend operator fun invoke(truck: Camion){
        repository.updateTruckIsActive(truck)
    }
}