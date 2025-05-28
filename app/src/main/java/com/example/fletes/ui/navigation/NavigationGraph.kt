package com.example.fletes.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fletes.R
import com.example.fletes.data.room.Destino
import com.example.fletes.ui.screenActiveDispatch.ActiveDispatchDetailsScreen
import com.example.fletes.ui.screenBuyData.BuyDataScreen
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
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navItems = listOf(
        CreateJourneyRoute,
        ActiveJourneysRoute,
        BuyDataRoute, // Added BuyDataRoute to the list
        CreateTruckRoute,
        CreateDispatchRoute
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        navItems.forEach { screen ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == screen::class.qualifiedName } == true
            NavigationBarItem(
                icon = {
                    when (screen) {
                        CreateJourneyRoute -> Icon(
                            painter = painterResource(R.drawable.ic_add_24),
                            contentDescription = "Home"
                        ) // Assuming Home for CreateJourney
                        ActiveJourneysRoute -> Icon(
                            painter = painterResource(R.drawable.ic_add_24),
                            contentDescription = "Active Journeys"
                        ) // Assuming List for ActiveJourneys
                        BuyDataRoute -> Icon(
                            painter = painterResource(R.drawable.ic_add_24),
                            contentDescription = "Buy Data"
                        ) // New Icon
                        CreateTruckRoute -> Icon(
                            painter = painterResource(R.drawable.ic_add_24),
                            contentDescription = "Trucks"
                        ) // Assuming LocalShipping for Trucks
                        CreateDispatchRoute -> Icon(
                            painter = painterResource(R.drawable.ic_add_24),
                            contentDescription = "Dispatch"
                        ) // Assuming Edit for Dispatch
                        else -> Icon(
                            painter = painterResource(R.drawable.ic_add_24),
                            contentDescription = "Unknown"
                        ) // Fallback
                    }
                },
                label = {
                    Text(
                        when (screen) {
                            CreateJourneyRoute -> "Home"
                            ActiveJourneysRoute -> "Journeys"
                            BuyDataRoute -> "Buy Data" // New Label
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





