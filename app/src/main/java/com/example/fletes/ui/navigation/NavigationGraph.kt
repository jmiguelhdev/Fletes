package com.example.fletes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fletes.ui.camion.CamionScreen
import com.example.fletes.ui.destino.DispatchScreen

@Composable
fun MyNavHost(
    navController: NavHostController,
    ) {
    NavHost(navController = navController, startDestination = DestinationScreenRoute) {
        composable<TruckScreenRoute> {
            CamionScreen() {
                navController.popBackStack()
            }
        }
        composable<DestinationScreenRoute> {
            DispatchScreen(){
                navController.popBackStack()
            }
        }
    }
}

