package com.example.fletes.domain

import android.util.Log
import com.example.fletes.data.repositories.CamionesRegistroRepository
import com.example.fletes.data.room.CamionesRegistro

class TripManager(
    private val trucksRepository: CamionesRegistroRepository
) {
    suspend fun saveNewTrip(camionesRegistro: CamionesRegistro) {
        val previousTrip = trucksRepository.getLastTripByCamionId(camionesRegistro.camionId)
        if (previousTrip != null) {
            val updatedPreviousTrip = previousTrip.copy(isLast = false)
            trucksRepository.updateCamionRegistro(updatedPreviousTrip)
        }
        val newTrip = calculateRendimiento(previousTrip, camionesRegistro)
        trucksRepository.insertCamionesRegistro(newTrip.copy(isLast = true))
    }

    private fun calculateRendimiento(
        previousTrip: CamionesRegistro?,
        currentTrip: CamionesRegistro
    ): CamionesRegistro {
        var newRendimiento: Double? = null
        if (previousTrip != null) {
            val kmDiff = currentTrip.kmSurtidor - previousTrip.kmSurtidor
            newRendimiento = if (currentTrip.litros == 0.0) {
                Log.e("TripManager", "Division by zero on trip ${currentTrip.id}")
                null
            } else {
                kmDiff / currentTrip.litros
            }
        }
        return currentTrip.copy(rendimiento = newRendimiento)
    }
}