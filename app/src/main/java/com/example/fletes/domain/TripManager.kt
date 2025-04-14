package com.example.fletes.domain

import CamionesRegistroRepository
import android.util.Log
import com.example.fletes.data.room.CamionesRegistro
import kotlinx.coroutines.flow.first

class TripManager(
    private val trucksRepository: CamionesRegistroRepository
) {
    suspend fun saveNewTrip(camionesRegistro: CamionesRegistro) {
        val previousTrip = trucksRepository.getLastTripByCamionId(camionesRegistro.camionId).first()
        if (previousTrip != null) {
            val updatedPreviousTrip = previousTrip.copy(isLast = false)
            trucksRepository.updateCamionesRegistro(updatedPreviousTrip)
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