package com.example.fletes.data.repositories.interfaces

import com.example.fletes.data.room.Buy
import kotlinx.coroutines.flow.Flow

interface BuyRepositoryInterface {
    suspend fun insertBuy(buy: Buy)
    suspend fun updateBuy(buy: Buy)
    fun getBuyForJourney(journeyId: Int): Flow<Buy?> // journeyId here refers to CamionesRegistro.id
}
