package com.example.fletes.domain

import com.example.fletes.data.repositories.interfaces.TrucksJourneyRepositoryInterface
import com.example.fletes.data.room.CamionesRegistro

class InsertJourneyUseCase(private val repository: TrucksJourneyRepositoryInterface) {
    suspend operator fun invoke(camionesRegistro: CamionesRegistro){
        repository.insertCamionesRegistro(camionesRegistro)
    }
}