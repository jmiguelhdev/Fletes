package com.example.fletes.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fletes.ui.navigation.Screen.DestinationScreenRoute

@Composable
fun MyNavHost(
    navController: NavHostController,
    startDestination: String,
    paddingValues: PaddingValues
    ) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(ScreenSRoutes.Screen1.route) {
//            CamionScreen() {
//                navController.popBackStack()
//            }
            Scren1()
        }
        composable(ScreenSRoutes.Screen2.route) {
//            DispatchScreen(){
//                navController.popBackStack()
//            }
            Scren2()
        }
    }
}

@Composable
fun Scren1(modifier: Modifier = Modifier) {
    Text("Screen 1")
}
@Composable
fun Scren2(modifier: Modifier = Modifier) {
    Text("Screen 2")
}



