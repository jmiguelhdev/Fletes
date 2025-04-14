package com.example.fletes.data.model.destinations

data class Destination(
    val id: Int,
    val comisionista: String? = null,
    val despacho: Int? = null,
    val localidad: String? = null,
)