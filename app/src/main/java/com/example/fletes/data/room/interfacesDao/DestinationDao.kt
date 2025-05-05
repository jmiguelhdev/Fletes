package com.example.fletes.data.room.interfacesDao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fletes.data.room.Destino
import kotlinx.coroutines.flow.Flow

@Dao
interface DestinationDao {
    // Destino
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertDestino(destino: Destino)

    @Delete
    suspend fun deleteDestino(destino: Destino)

    @Update
    suspend fun updateDestino(destino: Destino)

    @Query("SELECT * FROM destinos WHERE id = :id")
    fun getDestinoById(id: Int): Flow<Destino?>

    @Query("SELECT * FROM destinos")
    fun getAllDestinos(): Flow<List<Destino>>

    @Query("SELECT * FROM destinos WHERE is_active = 1")
    fun getActiveDestinos(): Flow<List<Destino>>

    @Query("SELECT * FROM destinos WHERE is_active = 1")
    fun getUnActiveDestinos(): Flow<List<Destino>>

    @Query("SELECT COUNT(*) FROM destinos WHERE is_active = 1")
    fun getActiveDestinosCount(): Flow<Int>
    @Query("SELECT DISTINCT comisionista FROM destinos WHERE comisionista LIKE '%' || :query || '%'")
    fun searchComisionista(query: String): Flow<List<String>>

    @Query("SELECT DISTINCT localidad FROM destinos WHERE localidad LIKE '%' || :query || '%'")
    fun searchLocalidad(query: String): Flow<List<String>>
}