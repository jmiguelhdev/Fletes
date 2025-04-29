package com.example.fletes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fletes.ui.camion.CamionScreen
import com.example.fletes.ui.dispatch.DispatchScreen
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
            TrucksDetailsScreen(
                onClickFab = {
                    navController.navigate(DispatchScreenRoute)
                },
                onClickAction = {
                    navController.navigate(TruckScreenRoute)
                }
            )
        }
        composable<DispatchScreenRoute> {
            DispatchScreen {
                navController.popBackStack()
            }
        }
        composable<TruckScreenRoute> {
            CamionScreen {
                navController.popBackStack()
            }
        }
    }
}





