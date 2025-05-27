package com.example.fletes.data.room.interfacesDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fletes.data.room.Buy
import kotlinx.coroutines.flow.Flow

@Dao
interface BuyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(buy: Buy)

    @Update
    suspend fun update(buy: Buy)

    @Query("SELECT * FROM buy_records WHERE camion_registro_id = :camionRegistroId")
    fun getBuyByCamionRegistroId(camionRegistroId: Int): Flow<Buy?>
}
