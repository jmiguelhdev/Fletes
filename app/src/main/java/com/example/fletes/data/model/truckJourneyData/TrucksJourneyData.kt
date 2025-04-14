package com.example.fletes.data.model.truckJourneyData

import java.time.LocalDate

data class TruckJourneyData(
    val id: Int,
    val createdAt: LocalDate,
    val kmCarga: Int,
    val kmDescarga: Int,
    val kmSurtidor: Int,
    val litros: Double,
    val precioGasOil: Double,
    val rendimiento: Double,
    val isLast: Boolean,
)