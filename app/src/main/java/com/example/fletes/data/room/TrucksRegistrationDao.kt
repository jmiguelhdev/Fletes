package com.example.fletes.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TrucksRegistrationDao {
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