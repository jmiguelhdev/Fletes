package com.example.fletes.ui.navigation

import com.example.fletes.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(
    val route: String,
    val title: String,
    val icon: Int
){
    @Serializable
    data object TruckScreenRoute : Screen (
        route = "truck_screen",
        title = "Camiones",
        icon = R.drawable.ic_time_to_leave_24
    )
    @Serializable
   data object DestinationScreenRoute : Screen(
        route = "destination_screen",
        title = "Destinos",
        icon = R.drawable.ic_location_pin_24
    )
}

sealed class ScreenSRoutes(
    val route: String,
    val title: String,
    val icon: Int
){
    data object Screen1: ScreenSRoutes(
        route = "screen1",
        title = "Camiones",
        icon = R.drawable.ic_time_to_leave_24
    )
    data object Screen2: ScreenSRoutes(
        route = "screen2",
        title = "Destinos",
        icon = R.drawable.ic_location_pin_24
    )
}




@Serializable
object TruckRegistrationScreenRoute

@Serializable
object TruckRegistrationDetailScreenRoute

