package com.example.fletes.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalDate

// Converters
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}

@Entity(tableName = "camiones")
data class Camion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "created_at") val createdAt: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "chofer_name") val choferName: String,
    @ColumnInfo(name = "chofer_dni") val choferDni: Int,
    @ColumnInfo(name = "patente_tractor") val patenteTractor: String,
    @ColumnInfo(name = "patente_jaula") val patenteJaula: String,
    @ColumnInfo(name = "km_service") val kmService: Int = 0
)

@Entity(tableName = "destinos")
data class Destino(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "created_at") val createdAt: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "comisionista") val comisionista: String,
    @ColumnInfo(name = "despacho") val despacho: Double = 0.0,
    @ColumnInfo(name = "localidad") val localidad: String,
    @ColumnInfo(name = "is_active") val isActive: Boolean = true,
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
    @ColumnInfo(name = "camion_id", index = true) val camionId: Int, // Foreign key to Camion
    @ColumnInfo(name = "destino_id", index = true) val destinoId: Int, // Foreign key to Destino
    @ColumnInfo(name = "created_at") val createdAt: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "km_carga") val kmCarga: Int, // temer en cuenta que se guarda como int
    @ColumnInfo(name = "km_descarga") val kmDescarga: Int,
    @ColumnInfo(name = "km_surtidor") val kmSurtidor: Int,
    @ColumnInfo(name = "litros") val litros: Double,
    @ColumnInfo(name = "is_last") val isLast: Boolean = false //new field
){
    fun getDistancia(): Int {
        return kmDescarga - kmCarga
    }
    fun getRateKmTimesLiters(): Double {
        return if (litros >0){
            getDistancia() / litros
        } else {
            0.0
        }
    }
}

/**
 * // Example (in a DAO, Repository, or ViewModel)
 * // Assuming you have a way to get CamionesRegistro entities from the database (e.g., a Flow, a list, etc.)
 * // Example of a function from the DAO class
 *
 * @Query("SELECT * FROM camiones_registro")
 * fun getAllCamionesRegistro(): Flow<List<CamionesRegistro>>
 *
 * //example to obtain the distance in a ViewModel class
 * viewModelScope.launch{
 *     camionRepository.getAllCamionesRegistro().collect { camionesRegistroList ->
 *             camionesRegistroList.forEach { camionesRegistro ->
 *                 val distancia = camionesRegistro.getDistancia()
 *                 // Now you have the calculated distance for this trip!
 *                 Log.d("CamionViewModel","distancia: $distancia")
 *             }
 *         }
 * }
 */