package com.example.fletes.domain

import com.example.fletes.data.repositories.implementations.TrucksJourneyRepositoryImp
import com.example.fletes.data.room.CamionesRegistro
import kotlinx.coroutines.flow.first

class TripManager(
    private val trucksRepository: TrucksJourneyRepositoryImp
) {
    suspend fun saveNewTrip(camionesRegistro: CamionesRegistro) {
        val previousTrip = trucksRepository.getLastTripByCamionId(camionesRegistro.camionId).first()
        if (previousTrip != null) {
            val updatedPreviousTrip = previousTrip.copy(isActive = false)
            trucksRepository.updateCamionesRegistro(updatedPreviousTrip)
        }
        //val newTrip = calculateRendimiento(previousTrip, camionesRegistro)
        //trucksRepository.insertCamionesRegistro(newTrip.copy(isLast = true))
    }


//    private fun calculateRendimiento(
//        previousTrip: CamionesRegistro?,
//        currentTrip: CamionesRegistro
//    ): CamionesRegistro {
//        var newRendimiento: Double? = null
//        if (previousTrip != null) {
//            val kmDiff = currentTrip.kmSurtidor - previousTrip.kmSurtidor
//            newRendimiento = if (currentTrip.litros == 0.0) {
//                Log.e("TripManager", "Division by zero on trip ${currentTrip.id}")
//                null
//            } else {
//                kmDiff / currentTrip.litros
//            }
//        }
//        return currentTrip.copy(rendimiento = newRendimiento)
//    }
}