package com.example.fletes.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "camiones")
data class Camion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "created_at") val createdAt: LocalDate,
    @ColumnInfo(name = "chofer_name") val choferName: String,
    @ColumnInfo(name = "patente_tractor") val patenteTractor: String,
    @ColumnInfo(name = "patente_jaula") val patenteJaula: String,
    @ColumnInfo(name = "km_service") val kmService: Int
)

@Entity(tableName = "destinos")
data class Destino(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val comisionista: String,
    val despacho: Double,
    val localidad: String,
)

@Entity(
    tableName = "camiones_registro",
    foreignKeys = [
        ForeignKey(
            entity = Camion::class,
            parentColumns = ["id"],
            childColumns = ["camion_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Destino::class,
            parentColumns = ["id"],
            childColumns = ["destino_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CamionesRegistro(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "camion_id") val camionId: Int, // Foreign key to Camion
    @ColumnInfo(name = "destino_id") val destinoId: Int, // Foreign key to Destino
    @ColumnInfo(name = "created_at") val createdAt: LocalDate,
    @ColumnInfo(name = "km_carga") val kmCarga: Int,
    @ColumnInfo(name = "km_descarga") val kmDescarga: Int,
    @ColumnInfo(name = "km_surtidor") val kmSurtidor: Int,
    val litros: Double,
    @ColumnInfo(name = "precio_gas_oil") val precioGasOil: Double,
)