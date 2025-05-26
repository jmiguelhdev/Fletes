package com.example.fletes.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fletes.data.room.Destino
import com.example.fletes.ui.screenActiveDispatch.ActiveDispatchDetailsScreen
import com.example.fletes.ui.screenDispatch.NewDispatchScreen
import com.example.fletes.ui.screenDispatch.NewDispatchViewModel
import com.example.fletes.ui.screenTruck.TruckScreen
import com.example.fletes.ui.screenTruck.TruckViewModel
import com.example.fletes.ui.screenTrucksJourney.JourneyRegistrationScreen
import com.example.fletes.ui.screenTrucksJourney.TruckJourneyViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyNavHost(
    navController: NavHostController,
) {
    val newDispatchViewModel: NewDispatchViewModel = koinViewModel()
    val truckViewModel: TruckViewModel = koinViewModel()
    val truckJourneyViewModel: TruckJourneyViewModel = koinViewModel()
    val activeTrucks by truckViewModel.camiones.collectAsState(emptyList())

    val allJourneys by truckJourneyViewModel.allJourneys.collectAsState(emptyList())
    val activeJourneys by truckJourneyViewModel.activeJourneys.collectAsState(emptyList())
    val truckJourneyUiState by truckJourneyViewModel.truckJourneyUiState.collectAsState()
    val listToShowJourney = if (truckJourneyUiState.checkedSwitch) activeJourneys else allJourneys

    val unActiveDestinations by newDispatchViewModel.unActiveDestinations.collectAsState(emptyList())
    val initialActiveDipatch = listOf(
        Destino(
            id = 1,
            comisionista = "",
            despacho = 0.0,
            localidad = "",
            isActive = true
        )
    )
    val activeDispatch by newDispatchViewModel.activeDispatch.collectAsState(initialActiveDipatch)


    LaunchedEffect(key1 = true) {
        truckViewModel.loadCamiones()  // Load the camiones when the parent composable is launched
        truckJourneyViewModel.loadJourneys()
        truckJourneyViewModel.loadActiveJourneys()
        newDispatchViewModel.loadDestinations()
    }

    Log.d("MyNavHost", "Active Trucks: $activeTrucks")
    Log.d("MyNavHost", "All Journeys: $allJourneys")

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CreateJourneyRoute,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable<CreateJourneyRoute> {
                if (activeTrucks.isNotEmpty()) {
                    ActiveDispatchDetailsScreen(
                        newDispatchViewModel = newDispatchViewModel,
                        truckViewModel = truckViewModel,
                        alltrucks = activeTrucks,
                        activeDispatch = activeDispatch,
                        unActiveDestinations = unActiveDestinations,
                        onClickFab = {
                            navController.navigate(CreateDispatchRoute)
                        },
                        onClickAction = {
                            navController.navigate(CreateTruckRoute)
                        }
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }

                }
            }
            composable<CreateDispatchRoute> {
                NewDispatchScreen(
                    viewModel = newDispatchViewModel,
                ) {
                    navController.popBackStack()
                }
            }
            composable<CreateTruckRoute> {
                TruckScreen(
                    truckViewModel = truckViewModel,
                ) {
                    navController.popBackStack()
                }
            }
            composable<ActiveJourneysRoute> {
                if (allJourneys.isNotEmpty()){
                    JourneyRegistrationScreen(
                        truckJourneyViewModel = truckJourneyViewModel,
                        allJourneys = listToShowJourney,
                        onCheckedChange = {
                            truckJourneyViewModel.onSwitchToggled(
                                it
                            )
                        },
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}





