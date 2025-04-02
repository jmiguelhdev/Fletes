package com.example.fletes.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MyNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen1Route) {
        composable<Screen1Route>{ Screen1() }
        composable<Screen2Route> { Screen2() }
    }
}

@Composable
fun Screen1() {
    Text("Screen 1")
}

@Composable
fun Screen2() {
    Text("Screen 2")
}