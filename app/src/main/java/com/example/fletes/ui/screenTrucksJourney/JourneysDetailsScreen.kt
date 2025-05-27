package com.example.fletes.ui.screenTrucksJourney

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fletes.data.model.truckJourneyData.TruckJourneyData
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.CamionesRegistro
import com.example.fletes.data.room.Destino
import com.example.fletes.data.room.JourneyWithAllDetails
import com.example.fletes.ui.screenTrucksJourney.components.JourneyCardItems
import com.example.fletes.ui.screenTrucksJourney.components.TopAppBarSwitch
import java.nio.file.WatchEvent
import java.time.format.DateTimeFormatter

@Composable
fun JourneyRegistrationScreen(
    truckJourneyViewModel: TruckJourneyViewModel,
    onCheckedChange: (Boolean) -> Unit,
) {
    val uiState = truckJourneyViewModel.truckJourneyUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBarSwitch(
                checked = uiState.value.checkedSwitch,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    ) { paddingValues ->
        val journeyToDisplayDistance = uiState.value.journeysForDisplay
        if (uiState.value.isLoading && uiState.value.journeysForDisplay.isEmpty()) { // Overall loading for the list
            CircularProgressIndicator(
                modifier = Modifier.padding(paddingValues)
            )
        } else if (uiState.value.isError) {
            Text(
                "Error loading journeys.",
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                items(uiState.value.journeysForDisplay, key = { journey -> journey.journey.id }) { journeySummary ->
                    val isCurrentJourneyExpanded =
                        uiState.value.expandedJourneyId == journeySummary.journey.id
                    Log.d(
                        "JourneyRegScreen",
                        "Journey: ${journeySummary.journey.id}, IsExpanded: $isCurrentJourneyExpanded, ExpandedID: ${uiState.value.expandedJourneyId}"
                    )
                    JourneyCard(
                        journeySummary = journeySummary.journey,
                        //uiState.value.distance
                        // Pass the Camion and Destino specific to the expanded journey
                        camion = journeySummary.camion,
                        destino = journeySummary.destino,
                        distance = journeySummary.calculatedDistance,
                        isExpanded = isCurrentJourneyExpanded,
                        truckJourneyDataForDisplayOrEdit = if (isCurrentJourneyExpanded) uiState.value.editableExpandedJourneyData else null,
                        isLoadingDetails = uiState.value.isLoading && isCurrentJourneyExpanded,
                        // For JourneyCardItems, we'll derive data from expandedDetails
                        onClick = {
                            Log.d("JourneyRegScreen", "onClick for ${journeySummary.journey.id}")
                            truckJourneyViewModel.onClickJourneyCard(journeySummary.journey.id)
                        },
                        onSaveClick = truckJourneyViewModel::saveExpandedJourneyDetails,
                        onCheckedChange = truckJourneyViewModel::updateExpandedIsActiveValue,
                    )
                }
            }
        }
    }


}

@Composable
fun JourneyCard(
    journeySummary: CamionesRegistro,
    camion: Camion?, // Now nullable, represents the camion of expandedDetails
    destino: Destino?, // Now nullable, represents the destino of expandedDetails
    distance: Int,
    isExpanded: Boolean,
    truckJourneyDataForDisplayOrEdit: TruckJourneyData?, // Renamed for clarity
    isLoadingDetails: Boolean,
    onSaveClick: () -> Unit,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            // AnimatedVisibility for the collapsed state
            AnimatedVisibility(visible = !isExpanded) {
                SingleDestinationCardFromDetails(
                    journey = journeySummary, // Or pass expandedDetails if it's the source of truth for display
                    camion = camion,     // This is the camion of the expanded item
                    destino = destino,   // This is the destino of the expanded item
                    distance = distance
                    )
            }
            // AnimatedVisibility for the expanded state
            AnimatedVisibility(visible = isExpanded) {
                Column { // Wrap expanded content in a Column for proper animation
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    if (isLoadingDetails) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else if (truckJourneyDataForDisplayOrEdit != null && camion != null && destino != null) {

                        JourneyCardItems(
                            camion = camion, // Pass the correct camion
                            destino = destino, // Pass the correct destino
                            truckJourneyData = truckJourneyDataForDisplayOrEdit, // Pass data derived from expandedDetails
                            onIsActiveChange = onCheckedChange
                        )
                        val isFormValid = truckJourneyDataForDisplayOrEdit.let { data ->
                            data.kmCargaData.errorMessage.isEmpty() &&
                            data.kmDescargaData.errorMessage.isEmpty() &&
                            data.kmSurtidorData.errorMessage.isEmpty() &&
                            data.litrosData.errorMessage.isEmpty()
                        }
                        SaveOrUpdateTripButton(
                            modifier = Modifier,
                            isActive = isFormValid,
                            onClickSaveOrUpdateTrip = {
                                onSaveClick()
                            }
                        )
                    } else {
                        Text("Details not available or error loading details.")
                    }
                }
            }
            //quiero agregar el calculo de distacincia cuando esta completo el viaje
        }
    }
}

// Renamed for clarity or adjust original SingleDestinationCard
@Composable
fun SingleDestinationCardFromDetails(
    modifier: Modifier = Modifier,
    journey: CamionesRegistro, // Still useful for things like createdAt
    camion: Camion?, // Nullable, as it might not be loaded yet or if not expanded
    destino: Destino?, // Nullable
    distance: Int,
    onClickCard: ((journey: CamionesRegistro) -> Unit)? = null // Make optional if not always used
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .then(if (onClickCard != null) Modifier.clickable { onClickCard(journey) } else Modifier),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Created At: ${journey.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE)}", // Example formatting
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    // Display placeholder if destino is not yet loaded for summary view
                    text = "Destino: ${destino?.localidad ?: journey.destinoId}", // Or "Loading..."
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Chofer: ${camion?.choferName ?: journey.camionId}", // Or "Loading..."
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(text = "Distancia: $distance km")
            }
        }
    }
}

@Composable
fun SaveOrUpdateTripButton(
    modifier: Modifier = Modifier,
    isActive: Boolean, //implementar
    onClickSaveOrUpdateTrip: () -> Unit = {},
) {
    OutlinedButton(
        onClick = { onClickSaveOrUpdateTrip() },
        modifier = modifier.fillMaxWidth(),
        enabled = isActive
    ) {
        Text(text = "Save or Update Trip")
    }

}





