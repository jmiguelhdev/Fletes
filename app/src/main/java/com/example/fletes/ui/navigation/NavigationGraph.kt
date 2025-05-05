package com.example.fletes.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fletes.ui.screenActiveDispatch.ActiveDispatchDetailsScreen
import com.example.fletes.ui.screenDispatch.NewDispatchScreen
import com.example.fletes.ui.screenDispatch.NewDispatchViewModel
import com.example.fletes.ui.screenTruck.TruckScreen
import com.example.fletes.ui.screenTruck.TruckViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyNavHost(
    navController: NavHostController,
    ) {

    val newDispatchViewModel: NewDispatchViewModel = koinViewModel()
    val truckViewModel: TruckViewModel = koinViewModel()
    val activeTrucks by truckViewModel.camiones.collectAsState(emptyList())

    // Load the camiones when the parent composable is launched
    LaunchedEffect(key1 = true) {
        truckViewModel.loadCamiones()
    }

    NavHost(
        navController = navController,
        startDestination = TrucksDetailScreenRoute,
    ) {
        composable<TrucksDetailScreenRoute> {
            if(activeTrucks.isNotEmpty()){
                ActiveDispatchDetailsScreen(
                    newDispatchViewModel = newDispatchViewModel,
                    alltrucks = activeTrucks,
                    onClickFab = {
                        navController.navigate(DispatchScreenRoute)
                    },
                    onClickAction = {
                        navController.navigate(TruckScreenRoute)
                    }
                )
            }else{
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }

            }
        }
        composable<DispatchScreenRoute> {
            NewDispatchScreen(
                viewModel = newDispatchViewModel,
            ) {
                navController.popBackStack()
            }
        }
        composable<TruckScreenRoute> {
            TruckScreen(
                truckViewModel = truckViewModel,
            ) {
                navController.popBackStack()
            }
        }
    }
}





