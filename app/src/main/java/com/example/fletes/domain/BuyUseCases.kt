package com.example.fletes.domain

import com.example.fletes.data.repositories.interfaces.BuyRepositoryInterface
import com.example.fletes.data.repositories.interfaces.TrucksJourneyRepositoryInterface
import com.example.fletes.data.room.Buy
import com.example.fletes.data.room.CamionesRegistro // Assuming this is needed for AddOrUpdateBuyDataUseCase
import com.example.fletes.data.room.JourneyWithBuyDetails
import kotlinx.coroutines.flow.Flow

// Use case to get active journeys for the new screen
class GetActiveJourneysForBuyScreenUseCase(
    private val trucksJourneyRepository: TrucksJourneyRepositoryInterface
) {
    operator fun invoke(): Flow<List<JourneyWithBuyDetails>> {
        // This assumes TrucksJourneyRepositoryInterface will be updated or already has a method
        // that calls trucksRegistrationDao.getActiveJourneysForBuyScreen()
        // For now, let's define it based on the plan.
        // The actual method in TrucksJourneyRepositoryInterface might need to be added if it doesn't exist.
        // For this subtask, we assume the repository will provide it.
        return trucksJourneyRepository.getActiveJourneysForBuyScreen()
    }
}

// Use case to get Buy data for a specific journey
class GetBuyForJourneyUseCase(
    private val buyRepository: BuyRepositoryInterface
) {
    operator fun invoke(journeyId: Int): Flow<Buy?> {
        return buyRepository.getBuyForJourney(journeyId)
    }
}

// Use case to add/update Buy data and mark CamionesRegistro as inactive
class AddOrUpdateBuyDataUseCase(
    private val buyRepository: BuyRepositoryInterface,
    private val trucksJourneyRepository: TrucksJourneyRepositoryInterface
) {
    suspend operator fun invoke(buy: Buy, journey: CamionesRegistro) {
        // Step 1: Insert or update the Buy record.
        // The 'buy' object passed in should have its isActive flag set to false 
        // by the ViewModel if the buy data is complete.
        buyRepository.insertBuy(buy) // insertBuy uses OnConflictStrategy.REPLACE

        // Step 2: Update the parent CamionesRegistro to be inactive.
        val updatedJourney = journey.copy(isActive = false)
        trucksJourneyRepository.updateCamionesRegistro(updatedJourney)
    }
}
