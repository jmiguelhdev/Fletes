package com.example.fletes.data.room

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow

// Database
@Database(
    entities = [Camion::class, Destino::class, CamionesRegistro::class],
    version = 1,
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

    @Delete
    suspend fun deleteCamion(camion: Camion)

    @Query("DELETE FROM camiones")
    suspend fun deleteAlllCamiones()

    @Query("SELECT * FROM camiones WHERE id = :id")
    suspend fun getCamionById(id: Int): Camion?

    @Query("SELECT * FROM camiones")
    fun getAllCamiones(): Flow<List<Camion>>

    // Destino
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDestino(destino: Destino)

    @Query("SELECT * FROM destinos WHERE id = :id")
    suspend fun getDestinoById(id: Int): Destino?

    @Query("SELECT * FROM destinos")
    fun getAllDestinos(): Flow<List<Destino>>

    // CamionesRegistro
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCamionesRegistro(camionesRegistro: CamionesRegistro)

    @Query("SELECT * FROM camiones_registro WHERE id = :id")
    suspend fun getCamionesRegistroById(id: Int): CamionesRegistro?

    @Query("SELECT * FROM camiones_registro")
    fun getAllCamionesRegistros(): Flow<List<CamionesRegistro>>

    @Query("SELECT * FROM camiones_registro WHERE camion_id = :camionId")
    fun getRegistrationsForTruck(camionId: Int): Flow<List<CamionesRegistro>>

    @Query("SELECT * FROM camiones_registro WHERE destino_id = :destinoId")
    fun getRegistrationsForDestination(destinoId: Int): Flow<List<CamionesRegistro>>

    @Query("SELECT * FROM camiones_registro")
    fun getAllRegistrations():Flow<List<CamionesRegistro>>
}