package com.example.fletes.ui.screenJourneySummary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button // Added for Retry button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fletes.data.room.JourneyWithBuyDetails // Though not directly used in this file, it's good for context
import com.example.fletes.ui.screenJourneySummary.components.JourneySummaryListItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneySummaryScreen(
    navController: NavController, // Not used in this screen's current definition, but good for navigation later
    viewModel: JourneySummaryViewModel = koinViewModel()
) {
    val journeys by viewModel.journeys.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Journey Summary") }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Apply padding from Scaffold here
            contentAlignment = Alignment.Center // Center content like CircularProgressIndicator or error message
        ) {
            if (isLoading && journeys.isEmpty()) {
                CircularProgressIndicator()
            } else if (error != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Error: $error")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        // viewModel.loadJourneys() // This would be ideal if loadJourneys was public
                        viewModel.clearError() // Fallback: clear error, user might navigate away or it gets reloaded by other means
                        // To implement a true retry, JourneySummaryViewModel.loadJourneys() should be callable from here
                        // or a new public method in ViewModel should trigger it.
                    }) {
                        Text("Retry")
                    }
                }
            } else if (journeys.isEmpty()) {
                Text(text = "No journeys found.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(), // LazyColumn itself fills the Box
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    // contentPadding is for padding *inside* the LazyColumn content area,
                    // if needed in addition to the Scaffold's padding applied to the Box.
                    // For this case, usually Modifier.padding(paddingValues) on the Box is sufficient.
                    // If specific internal padding for LazyColumn items is needed, use:
                    // contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp) (example)
                ) {
                    items(journeys, key = { it.journey.id }) { journey ->
                        JourneySummaryListItem(details = journey)
                    }
                }
            }
        }
    }
}
