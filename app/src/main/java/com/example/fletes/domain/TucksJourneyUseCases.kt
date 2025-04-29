package com.example.fletes.domain

import com.example.fletes.data.repositories.interfaces.TrucksJourneyRepositoryInterface
import com.example.fletes.data.room.CamionesRegistro
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
    operator fun invoke(): Flow<List<CamionesRegistro>> {
        return repository.getAllCamionesRegistrosStream()
    }

}