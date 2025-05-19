package com.example.fletes.ui.screenTrucksJourney

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fletes.data.model.truckJourneyData.TruckJourneyData
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.CamionesRegistro
import com.example.fletes.data.room.Destino
import com.example.fletes.ui.screenTrucksJourney.components.JourneyCardAnimated
import com.example.fletes.ui.screenTrucksJourney.components.JourneyCardItems
import com.example.fletes.ui.screenTrucksJourney.components.SingleDestinationCard

@Composable
fun JourneyRegistrationScreen(
    truckJourneyViewModel: TruckJourneyViewModel,
    allJourneys: List<CamionesRegistro>,
    ) {
    val uiState = truckJourneyViewModel.truckJourneyUiState.collectAsState()


    if (uiState.value.isLoading && allJourneys.isEmpty()) { // Overall loading for the list
        CircularProgressIndicator()
    } else if (uiState.value.isError) {
        Text("Error loading journeys.")
    } else {
        LazyColumn {
            items(allJourneys, key = { journey -> journey.id }) { journeySummary ->
                JourneyCard(
                    journeySummary = journeySummary, // Contains basic info for the card
                    isExpanded = uiState.value.expandedJourneyId == journeySummary.id,
                    camion = uiState.value.truckSelected,
                    destino = uiState.value.destinationSelected,
                    expandedDetails = if (uiState.value.expandedJourneyId == journeySummary.id) uiState.value.expandedJourneyDetails else null,
                    isLoadingDetails = uiState.value.isLoading && uiState.value.expandedJourneyId == journeySummary.id,
                    truckJourneyData = uiState.value.truckJourneyData,
                    onClick = {
                        truckJourneyViewModel.onClickJourneyCard(journeySummary.id)
                    }
                )
            }
        }
    }
}

@Composable
fun JourneyCard(
    journeySummary: CamionesRegistro, // Or a summary DTO
    camion: Camion,
    destino: Destino,
    isExpanded: Boolean,
    expandedDetails: CamionesRegistro?, // The full details for this specific card when expanded
    isLoadingDetails: Boolean,
    truckJourneyData: TruckJourneyData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SingleDestinationCard(
                modifier = Modifier,
                journey = journeySummary,
                camion = camion,
                destino = destino
            ) { }

            if (isExpanded) {
                // This content is shown only for the expanded card
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                if (isLoadingDetails) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (expandedDetails != null) {
                    JourneyCardItems(
                        camion = camion,
                        destino = destino,
                        truckJourneyData = truckJourneyData
                    )
                } else {
                    Text("Details not available or error loading details.")
                }
            }
        }
    }
}
@Composable
fun SaveOrUpdateTripButton(
    modifier: Modifier = Modifier,
    destinationId: Int,
    isActive: Boolean, //implementar
    onClickSaveOrUpdateTrip: (destinoId: Int) -> Unit = {},
) {
    OutlinedButton(
        onClick = { onClickSaveOrUpdateTrip(destinationId) },
        modifier = modifier.fillMaxWidth(),
        enabled = isActive
    ) {
        Text(text = "Save or Update Trip")
    }

}





