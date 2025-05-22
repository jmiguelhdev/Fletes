package com.example.fletes.domain

import com.example.fletes.data.repositories.interfaces.DestinationRepositoryInterface
import com.example.fletes.data.repositories.interfaces.TruckRepositoryInterface
import com.example.fletes.data.repositories.interfaces.TrucksJourneyRepositoryInterface
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.CamionesRegistro
import com.example.fletes.data.room.Destino
import com.example.fletes.data.room.JourneyWithDetails // Add this
import kotlinx.coroutines.flow.Flow

class InsertJourneyUseCase(private val repository: TrucksJourneyRepositoryInterface) {
    suspend operator fun invoke(camionesRegistro: CamionesRegistro){
        repository.insertCamionesRegistro(camionesRegistro)
    }
}

class UpdateJourneyUseCase(private val repository: TrucksJourneyRepositoryInterface) {
    suspend operator fun invoke(camionesRegistro: CamionesRegistro){
        repository.updateCamionesRegistro(camionesRegistro)
    }
}

class GetAllJourneyUseCase(private val repository: TrucksJourneyRepositoryInterface) {
    operator fun invoke(): Flow<List<JourneyWithDetails>> { // Changed return type
        return repository.getAllJourneysWithDetails()    // Changed method call
    }
}

class GetTruckByIdUseCase(private val repository: TruckRepositoryInterface) {
    suspend operator fun invoke(id: Int): Camion? {
        return repository.getCamionById(id)
    }
}

class GetDestinationByIdUseCase(private val repository: DestinationRepositoryInterface) {
     suspend operator fun invoke(id: Int): Flow<Destino?> {
        return repository.getDestinoStream(id)
    }
}

class GetTruckJourneyByIdUseCase(private val repository: TrucksJourneyRepositoryInterface) {
    suspend operator fun invoke(id: Int): Flow<CamionesRegistro?> {
        return repository.getCamionesRegistroStream(id)
    }
}

