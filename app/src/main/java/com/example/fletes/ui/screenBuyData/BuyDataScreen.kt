package com.example.fletes.ui.screenBuyData

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.foundation.layout.Arrangement // New import
import androidx.compose.foundation.layout.Box // New import
import androidx.compose.foundation.layout.Column // New import
import androidx.compose.foundation.layout.fillMaxSize // New import
import androidx.compose.foundation.layout.padding // New import
import androidx.compose.foundation.lazy.LazyColumn // New import
import androidx.compose.foundation.lazy.items // New import
import androidx.compose.material3.CircularProgressIndicator // New import
import androidx.compose.foundation.clickable // New import
import androidx.compose.foundation.layout.Spacer // New import
import androidx.compose.foundation.layout.fillMaxWidth // New import
import androidx.compose.foundation.layout.height // New import
import androidx.compose.foundation.rememberScrollState // New import
import androidx.compose.foundation.text.KeyboardOptions // New import
import androidx.compose.foundation.verticalScroll // New import
import androidx.compose.material3.Button // New import
import androidx.compose.material3.Card // New import
import androidx.compose.material3.CardDefaults // New import
import androidx.compose.material3.MaterialTheme // New import
import androidx.compose.material3.OutlinedButton // New import
import androidx.compose.material3.OutlinedTextField // New import
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment // New import
import androidx.compose.ui.Modifier // New import
import androidx.compose.ui.text.input.KeyboardType // New import
import androidx.compose.ui.unit.dp // New import
import androidx.navigation.NavController
import com.example.fletes.data.room.JourneyWithBuyDetails // Ensure this is imported
import org.koin.androidx.compose.koinViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter // New import for DateTimeFormatter

// Actual implementation for BuyJourneyListItem
@Composable
fun BuyJourneyListItem(
    journeyWithDetails: JourneyWithBuyDetails,
    onClick: () -> Unit,
    modifier: Modifier = Modifier // Added modifier
) {
    Card(
        modifier = modifier // Use the passed modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Date: ${journeyWithDetails.journey.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE)}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Driver: ${journeyWithDetails.camion.choferName}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Destination: ${journeyWithDetails.destino.localidad}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Distance: ${journeyWithDetails.calculatedDistance} km", style = MaterialTheme.typography.bodySmall)
            Text(text = "Comisionista: ${journeyWithDetails.destino.comisionista}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Despacho: ${journeyWithDetails.destino.despacho}", style = MaterialTheme.typography.bodySmall)
            // Note: calculatedRateKmLiters is not requested for this list item display yet.
        }
    }
}

// Actual implementation for ActiveJourneysList
@Composable
fun ActiveJourneysList(
    journeys: List<JourneyWithBuyDetails>,
    isLoading: Boolean,
    error: String?,
    onJourneyClick: (JourneyWithBuyDetails) -> Unit,
    modifier: Modifier = Modifier // Added modifier parameter
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // Apply padding around the Box content
    ) {
        if (isLoading && journeys.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (error != null) {
            Text(
                text = error,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (journeys.isEmpty()) {
            Text(
                text = "No active journeys found.",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Spacing between items
            ) {
                items(journeys, key = { it.journey.id }) { journeyItem ->
                    BuyJourneyListItem(
                        journeyWithDetails = journeyItem,
                        onClick = { onJourneyClick(journeyItem) }
                    )
                }
            }
        }
    }
}

@Composable
fun BuyDataForm(
    journeyDetails: JourneyWithBuyDetails,
    formData: BuyFormData,
    isSaving: Boolean,
    onKgChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onKgFaenaChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize() // Use fillMaxSize from modifier
            .verticalScroll(rememberScrollState()) // Make content scrollable
            .padding(16.dp) // Add padding around the column content
    ) {
        Text("Processing Buy Data for Journey:", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Date: ${journeyDetails.journey.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE)}", style = MaterialTheme.typography.titleMedium)
        Text("Driver: ${journeyDetails.camion.choferName}", style = MaterialTheme.typography.titleMedium)
        Text("Destination: ${journeyDetails.destino.localidad}", style = MaterialTheme.typography.titleMedium)
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = formData.kg,
            onValueChange = onKgChange,
            label = { Text("Kg") },
            isError = formData.kgError.isNotBlank(),
            supportingText = { if (formData.kgError.isNotBlank()) Text(formData.kgError) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = formData.price,
            onValueChange = onPriceChange,
            label = { Text("Price per Kg") },
            isError = formData.priceError.isNotBlank(),
            supportingText = { if (formData.priceError.isNotBlank()) Text(formData.priceError) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = formData.kgFaena,
            onValueChange = onKgFaenaChange,
            label = { Text("Kg Faena") },
            isError = formData.kgFaenaError.isNotBlank(),
            supportingText = { if (formData.kgFaenaError.isNotBlank()) Text(formData.kgFaenaError) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End // Align buttons to the end
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.padding(end = 8.dp),
                enabled = !isSaving
            ) {
                Text("Cancel")
            }
            Button(
                onClick = onSave,
                enabled = !isSaving && formData.kgError.isBlank() &&
                          formData.priceError.isBlank() && formData.kgFaenaError.isBlank() &&
                          formData.kg.isNotBlank() && formData.price.isNotBlank() && formData.kgFaena.isNotBlank() // Ensure fields are not blank for saving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.height(24.dp)) // Show progress in button
                } else {
                    Text("Save Buy Data")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyDataScreen(
    navController: NavController, // Keep NavController for potential future use, though not used in this snippet
    viewModel: BuyDataViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            scope.launch {
                snackbarHostState.showSnackbar(message = it)
                viewModel.clearError() // Clear error after showing
            }
        }
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            scope.launch {
                snackbarHostState.showSnackbar(message = "Buy data saved successfully!")
                viewModel.clearSaveSuccessFlag()
            }
            // Optionally navigate or change screen state further if needed
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Process Buy Data") })
        }
    ) { paddingValues ->
        // Apply paddingValues to the content area
        // For now, the placeholders don't use it, but real implementations should.
        if (uiState.selectedJourney == null) {
            ActiveJourneysList(
                journeys = uiState.journeys,
                isLoading = uiState.isLoadingJourneys,
                error = uiState.error,
                onJourneyClick = viewModel::selectJourney,
                modifier = Modifier.padding(paddingValues) // Pass paddingValues
            )
        } else {
            BuyDataForm(
                journeyDetails = uiState.selectedJourney!!, // !! is safe due to selectedJourney != null check
                formData = uiState.buyFormData,
                isSaving = uiState.isSaving,
                onKgChange = viewModel::updateBuyKg,
                onPriceChange = viewModel::updateBuyPrice,
                onKgFaenaChange = viewModel::updateBuyKgFaena,
                onSave = viewModel::saveBuyData,
                onDismiss = { viewModel.selectJourney(null) },
                modifier = Modifier.padding(paddingValues) // Pass paddingValues
            )
        }
    }
}
