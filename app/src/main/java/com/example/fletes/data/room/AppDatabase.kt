package com.example.fletes.data.room

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Database
@Database(
    entities = [Camion::class, Destino::class, CamionesRegistro::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}

// DAOs
@Dao
interface AppDao {
    // Camion
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCamion(camion: Camion)

    @Update
    suspend fun updateCamion(camion: Camion)

    @Delete
    suspend fun deleteCamion(camion: Camion)

    @Query("DELETE FROM camiones")
    suspend fun deleteAlllCamiones()

    @Query("SELECT * FROM camiones WHERE id = :id")
    suspend fun getCamionById(id: Int): Camion?

    @Query("SELECT * FROM camiones")
    fun getAllCamiones(): Flow<List<Camion>>

    // Destino
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDestino(destino: Destino)

    @Delete
    suspend fun deleteDestino(destino: Destino)

    @Update
    suspend fun updateDestino(destino: Destino)

    @Query("SELECT * FROM destinos WHERE id = :id")
    fun getDestinoById(id: Int): Flow<Destino?>

    @Query("SELECT * FROM destinos")
    fun getAllDestinos(): Flow<List<Destino>>

    @Query("SELECT DISTINCT comisionista FROM destinos WHERE comisionista LIKE '%' || :query || '%'")
    fun searchComisionista(query: String): Flow<List<String>>

    @Query("SELECT DISTINCT localidad FROM destinos WHERE localidad LIKE '%' || :query || '%'")
    fun searchLocalidad(query: String): Flow<List<String>>

    // CamionesRegistro
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCamionesRegistro(camionesRegistro: CamionesRegistro)

    @Update
    suspend fun updateCamionRegistro(camionesRegistro: CamionesRegistro)

    @Query("SELECT * FROM camiones_registro WHERE id = :id")
    fun getCamionesRegistroById(id: Int): Flow<CamionesRegistro?>

    //Example of how to get the last 7 rendimiento for a given camionId
    @Query("SELECT * FROM camiones_registro WHERE camion_id = :camionId ORDER BY created_at DESC LIMIT 7")
    fun getLast7RendimientoByCamionId(camionId:Int): Flow<List<CamionesRegistro>>

    @Query("SELECT * FROM camiones_registro WHERE camion_id = :camionId ORDER BY created_at DESC LIMIT 1")
    fun getLastTripByCamionId(camionId: Int): Flow<CamionesRegistro>


    @Query("SELECT * FROM camiones_registro")
    fun getAllCamionesRegistros(): Flow<List<CamionesRegistro>>

    @Query("SELECT * FROM camiones_registro WHERE camion_id = :camionId")
    fun getRegistrationsForTruck(camionId: Int): Flow<List<CamionesRegistro>>

    @Query("SELECT * FROM camiones_registro WHERE destino_id = :destinoId")
    fun getRegistrationsForDestination(destinoId: Int): Flow<List<CamionesRegistro>>

    @Query("SELECT * FROM camiones_registro")
    fun getAllRegistrations():Flow<List<CamionesRegistro>>
}