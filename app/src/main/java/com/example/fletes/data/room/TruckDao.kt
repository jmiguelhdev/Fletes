package com.example.fletes.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckDAo {
    // Camion
    @Insert(onConflict = OnConflictStrategy.REPLACE)
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
}