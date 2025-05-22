package com.example.fletes.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
        newDispatchViewModel.loadDestinations()
    }

    Log.d("MyNavHost", "Active Trucks: $activeTrucks")
    Log.d("MyNavHost", "All Journeys: $allJourneys")

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet { // Use ModalDrawerSheet for Material 3 guidelines
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Fletes App", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp))
                    Divider(modifier = Modifier.padding(bottom = 16.dp))

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    // currentRoute can be directly compared to the route object
                    val currentRoute = navBackStackEntry?.destination?.route

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.LocalShipping, contentDescription = "Trucks") },
                        label = { Text("Trucks") },
                        selected = currentRoute == CreateTruckRoute,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(CreateTruckRoute) {
                                popUpTo(navController.graph.startDestinationRoute!!) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    // Add other items here if needed in the future
                    // Example:
                    // Spacer(modifier = Modifier.height(8.dp))
                    // NavigationDrawerItem(
                    //     icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    //     label = { Text("Settings") },
                    //     selected = currentRoute == "SettingsRoute", // Replace "SettingsRoute" with actual route
                    //     onClick = {
                    //         scope.launch { drawerState.close() }
                    //         // navController.navigate("SettingsRoute")
                    //     }
                    // )
                }
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = CreateJourneyRoute,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<CreateJourneyRoute> {
                    if (activeTrucks.isNotEmpty()) {
                    ActiveDispatchDetailsScreen(
                        newDispatchViewModel = newDispatchViewModel,
                        truckViewModel = truckViewModel,
                        alltrucks = activeTrucks,
                        activeDispatch = activeDispatch,
                        unActiveDestinations = unActiveDestinations,
                        drawerState = drawerState, // Add this
                        scope = scope,             // Add this
                        onClickFab = {
                            navController.navigate(CreateDispatchRoute)
                        },
                        onClickAction = {
                            navController.navigate(CreateTruckRoute) // This action might need review
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
                    drawerState = drawerState, // Pass drawerState
                    scope = scope,           // Pass scope
                    onNavBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable<ActiveJourneysRoute> {
                if (allJourneys.isNotEmpty()){
                    JourneyRegistrationScreen(
                        truckJourneyViewModel = truckJourneyViewModel,
                        allJourneys = allJourneys,
                        drawerState = drawerState, // Add this
                        scope = scope              // Add this
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





