package com.example.fletes.data.model.transportUnit

import java.time.LocalDate

data class TransportUnit(
    val id: Int,
    val createdAt: LocalDate,
    val choferName: String,
    val choferDni: Int,
    val choferImporte: Double,
    val patenteTractor: String,
    val patenteJaula: String,
    val kmService: Int
)