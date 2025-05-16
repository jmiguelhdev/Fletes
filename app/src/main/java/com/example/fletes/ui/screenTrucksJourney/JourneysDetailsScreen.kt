package com.example.fletes.ui.screenTrucksJourney

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.fletes.ui.screenTrucksJourney.components.JourneyCardAnimated

@Composable
fun JourneyRegistrationScreen(truckJourneyViewModel: TruckJourneyViewModel) {
    val listOfActiveDestinations = truckJourneyViewModel.allJourneys.collectAsState()
    val uiState = truckJourneyViewModel.truckJourneyUiState.collectAsState()

    LazyColumn { 
        items(listOfActiveDestinations){
            JourneyCardAnimated(
                expanded = TODO(),
                destino = TODO(),
                camion = TODO(),
                truckJourneyData = TODO(),
                modifier = TODO()
            )
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





