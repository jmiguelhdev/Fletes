package com.example.fletes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fletes.ui.screenTruck.TruckScreen
import com.example.fletes.ui.screenDispatch.NewDispatchScreen
import com.example.fletes.ui.screenActiveDispatch.ActiveDispatchDetailsScreen

@Composable
fun MyNavHost(
    navController: NavHostController,
    ) {
    NavHost(
        navController = navController,
        startDestination = TrucksDetailScreenRoute,
    ) {
        composable<TrucksDetailScreenRoute> {
            ActiveDispatchDetailsScreen(
                onClickFab = {
                    navController.navigate(DispatchScreenRoute)
                },
                onClickAction = {
                    navController.navigate(TruckScreenRoute)
                }
            )
        }
        composable<DispatchScreenRoute> {
            NewDispatchScreen {
                navController.popBackStack()
            }
        }
        composable<TruckScreenRoute> {
            TruckScreen {
                navController.popBackStack()
            }
        }
    }
}





