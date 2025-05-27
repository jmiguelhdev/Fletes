package com.example.fletes.data.repositories.implementations

import com.example.fletes.data.room.Buy
import com.example.fletes.data.room.interfacesDao.BuyDao
import com.example.fletes.data.repositories.interfaces.BuyRepositoryInterface
import kotlinx.coroutines.flow.Flow

class BuyRepositoryImpl(private val buyDao: BuyDao) : BuyRepositoryInterface {
    override suspend fun insertBuy(buy: Buy) {
        buyDao.insert(buy)
    }

    override suspend fun updateBuy(buy: Buy) {
        buyDao.update(buy)
    }

    override fun getBuyForJourney(journeyId: Int): Flow<Buy?> {
        return buyDao.getBuyByCamionRegistroId(journeyId)
    }
}
