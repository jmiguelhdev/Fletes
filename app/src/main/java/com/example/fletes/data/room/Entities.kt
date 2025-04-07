package com.example.fletes.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

// Converters
class Converters {
    @androidx.room.TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @androidx.room.TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}
@Entity(tableName = "camiones")
data class Camion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "created_at") val createdAt: LocalDate,
    @ColumnInfo(name = "chofer_name") val choferName: String,
    @ColumnInfo(name = "chofer_dni") val choferDni: Int,
    @ColumnInfo(name = "patente_tractor") val patenteTractor: String,
    @ColumnInfo(name = "patente_jaula") val patenteJaula: String,
    @ColumnInfo(name = "km_service") val kmService: Int
)

@Entity(tableName = "destinos")
data class Destino(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "comisionista") val comisionista: String,
    @ColumnInfo(name = "despacho") val despacho: Double,
    @ColumnInfo(name = "localidad") val localidad: String,
    @ColumnInfo(name = "distancia") val distancia: Double // Distance to this destination
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
    @ColumnInfo(name = "litros") val litros: Double,
    @ColumnInfo(name = "precio_gas_oil") val precioGasOil: Double,
)