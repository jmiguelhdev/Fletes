package com.example.fletes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fletes.ui.camion.CamionScreen
import com.example.fletes.ui.camion.CamionViewModel

@Composable
fun MyNavHost(navController: NavHostController, camionViewModel: CamionViewModel) {
    NavHost(navController = navController, startDestination = TruckScreenRoute) {
        composable<TruckScreenRoute> {
            CamionScreen(camionViewModel) {
                navController.popBackStack()
            }
        }
        composable<DestinationScreenRoute> { }
    }
}

