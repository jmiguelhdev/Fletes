package com.example.fletes.domain

import com.example.fletes.data.repositories.interfaces.TrucksJourneyRepositoryInterface
import com.example.fletes.data.room.CamionesRegistro

class InsertJourneyUseCases(private val repository: TrucksJourneyRepositoryInterface) {
    suspend operator fun invoke(registro: CamionesRegistro) {
            return repository.insertCamionesRegistro(registro)
    }
}