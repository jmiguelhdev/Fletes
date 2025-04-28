package com.example.fletes.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fletes.data.room.interfacesDao.DestinationDao
import com.example.fletes.data.room.interfacesDao.TruckDao
import com.example.fletes.data.room.interfacesDao.TrucksRegistrationDao

// Database
@Database(
    entities = [Camion::class, Destino::class, CamionesRegistro::class],
    version = 7,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun truckDao(): TruckDao
    abstract fun destinationDao(): DestinationDao
    abstract fun trucksRegistrationDao(): TrucksRegistrationDao
}

// DAOs

//@Dao
//interface AppDao{
//    // Camion
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertTruck(camion: Camion)
//
//    @Update
//    suspend fun updateTruck(camion: Camion)
//
//    @Delete
//    suspend fun deleteCamion(camion: Camion)
//
//    @Query("DELETE FROM camiones")
//    suspend fun deleteAllTrucks()
//
//    @Query("SELECT * FROM camiones WHERE id = :id")
//    suspend fun getTruckById(id: Int): Camion?
//
//    @Query("SELECT * FROM camiones")
//    fun getAllTrucks(): Flow<List<Camion>>
//
//
//    // Destino
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertDestino(destino: Destino)
//
//    @Delete
//    suspend fun deleteDestino(destino: Destino)
//
//    @Update
//    suspend fun updateDestino(destino: Destino)
//
//    @Query("SELECT * FROM destinos WHERE id = :id")
//    fun getDestinoById(id: Int): Flow<Destino?>
//
//    @Query("SELECT * FROM destinos")
//    fun getAllDestinos(): Flow<List<Destino>>
//
//    @Query("SELECT DISTINCT comisionista FROM destinos WHERE comisionista LIKE '%' || :query || '%'")
//    fun searchComisionista(query: String): Flow<List<String>>
//
//    @Query("SELECT DISTINCT localidad FROM destinos WHERE localidad LIKE '%' || :query || '%'")
//    fun searchLocalidad(query: String): Flow<List<String>>
//
//    // CamionesRegistro
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertCamionesRegistro(camionesRegistro: CamionesRegistro)
//
//    @Update
//    suspend fun updateCamionRegistro(camionesRegistro: CamionesRegistro)
//
//    @Query("SELECT * FROM camiones_registro WHERE id = :id")
//    fun getCamionesRegistroById(id: Int): Flow<CamionesRegistro?>
//
//    //Example of how to get the last 7 rendimiento for a given camionId
//    @Query("SELECT * FROM camiones_registro WHERE camion_id = :camionId ORDER BY created_at DESC LIMIT 7")
//    fun getLast7RendimientoByCamionId(camionId:Int): Flow<List<CamionesRegistro>>
//
//    @Query("SELECT * FROM camiones_registro WHERE camion_id = :camionId ORDER BY created_at DESC LIMIT 1")
//    fun getLastTripByCamionId(camionId: Int): Flow<CamionesRegistro>
//
//
//    @Query("SELECT * FROM camiones_registro")
//    fun getAllCamionesRegistros(): Flow<List<CamionesRegistro>>
//
//    @Query("SELECT * FROM camiones_registro WHERE camion_id = :camionId")
//    fun getRegistrationsForTruck(camionId: Int): Flow<List<CamionesRegistro>>
//
//    @Query("SELECT * FROM camiones_registro WHERE destino_id = :destinoId")
//    fun getRegistrationsForDestination(destinoId: Int): Flow<List<CamionesRegistro>>
//
//    @Query("SELECT * FROM camiones_registro")
//    fun getAllRegistrations():Flow<List<CamionesRegistro>>
//}
