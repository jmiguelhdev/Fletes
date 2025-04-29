package com.example.fletes.data.room.interfacesDao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fletes.data.room.Camion
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckDao {
    // Camion
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertTruck(camion: Camion)

    @Update
    suspend fun updateTruck(camion: Camion)

    @Delete
    suspend fun deleteCamion(camion: Camion)

    @Query("DELETE FROM camiones")
    suspend fun deleteAllTrucks()

    @Query("SELECT * FROM camiones WHERE id = :id")
    suspend fun getTruckById(id: Int): Camion?

    @Query("SELECT * FROM camiones")
    fun getAllTrucks(): Flow<List<Camion>>

    @Query("UPDATE camiones SET is_active = :isActive WHERE id = :truckId")
    suspend fun updateTruckIsActive(truckId: Int, isActive: Boolean)

    @Query("SELECT * FROM camiones WHERE is_active = 1")
    fun getActiveTrucks(): Flow<List<Camion>>

}