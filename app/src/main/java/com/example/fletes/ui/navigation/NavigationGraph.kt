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
import androidx.compose.material.icons.Icons // New import
import androidx.compose.material.icons.filled.Analytics // Placeholder for Summary icon
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingCart // New import
import androidx.compose.material3.Icon // New import
import androidx.compose.material3.NavigationBar // New import
import androidx.compose.material3.NavigationBarItem // New import
import androidx.compose.material3.Text // New import
import androidx.navigation.NavDestination.Companion.hierarchy // New import
import androidx.navigation.NavGraph.Companion.findStartDestination // New import
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState // New import
import com.example.fletes.data.room.Destino
import com.example.fletes.ui.screenActiveDispatch.ActiveDispatchDetailsScreen
import com.example.fletes.ui.screenBuyData.BuyDataScreen // New import
import com.example.fletes.ui.screenDispatch.NewDispatchScreen
// Removed duplicate NewDispatchViewModel import if present, ensure single import
import com.example.fletes.ui.screenTruck.TruckScreen
import com.example.fletes.ui.screenTruck.TruckViewModel
// Added JourneySummaryScreen import
import com.example.fletes.ui.screenJourneySummary.JourneySummaryScreen
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

    val truckJourneyUiState by truckJourneyViewModel.truckJourneyUiState.collectAsState()

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
        newDispatchViewModel.loadDestinations()
    }



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
                // JourneyRegistrationScreen internally handles its loading/empty/error states
                // based on truckJourneyUiState, which is collected above.
                JourneyRegistrationScreen(
                    truckJourneyViewModel = truckJourneyViewModel,
                    onCheckedChange = truckJourneyViewModel::onSwitchToggled // Use stable reference
                )
            }
            composable<BuyDataRoute> { // New composable for BuyDataScreen
                BuyDataScreen(navController = navController)
            }
            composable<JourneySummaryRoute> { // New composable for JourneySummaryScreen
                JourneySummaryScreen(navController = navController)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navItems = listOf(
        CreateJourneyRoute,
        ActiveJourneysRoute,
        BuyDataRoute, // Added BuyDataRoute to the list
        JourneySummaryRoute, // Added JourneySummaryRoute to the list
        CreateTruckRoute,
        CreateDispatchRoute
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        navItems.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen::class.qualifiedName } == true
            NavigationBarItem(
                icon = {
                    when (screen) {
                        CreateJourneyRoute -> Icon(Icons.Filled.Home, contentDescription = "Home") // Assuming Home for CreateJourney
                        ActiveJourneysRoute -> Icon(Icons.Filled.List, contentDescription = "Active Journeys") // Assuming List for ActiveJourneys
                        BuyDataRoute -> Icon(Icons.Filled.ShoppingCart, contentDescription = "Buy Data") // New Icon
                        JourneySummaryRoute -> Icon(Icons.Filled.Analytics, contentDescription = "Summary") // New Icon for Summary
                        CreateTruckRoute -> Icon(Icons.Filled.LocalShipping, contentDescription = "Trucks") // Assuming LocalShipping for Trucks
                        CreateDispatchRoute -> Icon(Icons.Filled.Edit, contentDescription = "Dispatch") // Assuming Edit for Dispatch
                        else -> Icon(Icons.Filled.Error, contentDescription = "Unknown") // Fallback
                    }
                },
                label = {
                    Text(
                        when (screen) {
                            CreateJourneyRoute -> "Home"
                            ActiveJourneysRoute -> "Journeys"
                            BuyDataRoute -> "Buy Data" // New Label
                            JourneySummaryRoute -> "Summary" // New Label for Summary
                            CreateTruckRoute -> "Trucks"
                            CreateDispatchRoute -> "Dispatch"
                            else -> "Unknown"
                        }
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(screen) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}





