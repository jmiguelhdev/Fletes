package com.example.fletes.data.model

import java.time.LocalDate

data class CamionesRegistro(
    val id: Int,
    val createdAt: LocalDate,
    val kmCarga: Int,
    val kmDescarga: Int,
    val kmSurtidor: Int,
    val litros: Double,
    val precioGasOil: Double,
)

data class Camion(
    val id: Int,
    val createdAt: LocalDate,
    val choferName: String,
    val choferDni: Int,
    val choferImporte: Double,
    val patenteTractor: String,
    val patenteJaula: String,
    val kmService: Int
)

data class Destino(
    val id: Int,
    val comisionista: String? = null,
    val despacho: Int? = null,
    val localidad: String? = null,
    val distancia: Double,
)