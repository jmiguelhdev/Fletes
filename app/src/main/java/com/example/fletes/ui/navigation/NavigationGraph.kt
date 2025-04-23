package com.example.fletes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fletes.ui.camion.CamionScreen
import com.example.fletes.ui.camion.CamionViewModel
import com.example.fletes.ui.destino.DispatchScreen
import com.example.fletes.ui.trucksDetails.TrucksDetailsScreen

@Composable
fun MyNavHost(
    navController: NavHostController,
    ) {
    NavHost(
        navController = navController,
        startDestination = TrucksDetailScreenRoute,
    ) {
        composable<TrucksDetailScreenRoute> {
            TrucksDetailsScreen(){
                navController.navigate(DispatchScreenRoute)
            }
        }
        composable<DispatchScreenRoute> {
            DispatchScreen {

            }
        }
        composable<TruckScreenRoute> {
            CamionScreen {
                navController.popBackStack()
            }
        }
    }
}





