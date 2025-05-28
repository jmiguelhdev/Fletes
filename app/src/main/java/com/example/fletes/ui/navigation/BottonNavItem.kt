package com.example.fletes.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fletes.R

sealed class BottomNavItem(val route: Any, val iconResourceId: Int, val label: String) {
    object TrucksDetail : BottomNavItem(
        CreateJourneyRoute,
         R.drawable.ic_add_24,
        "Create Journey"
    )

    object Dispatch : BottomNavItem(
        CreateDispatchRoute,
        R.drawable.ic_location_pin_24,
        "Create Dispatch"
    )

    object Trucks : BottomNavItem(
        CreateTruckRoute,
        R.drawable.icl_shipping_24,
        "Create Trucks"
    )
    object ActiveJourneys : BottomNavItem(
        ActiveJourneysRoute,
        R.drawable.ic_format_list_numbered_24,
        "Active Journeys"
    )
    object JourneySummary : BottomNavItem(
        JourneySummaryRoute, // Route from routes.kt
        R.drawable.icl_shipping_24, // Placeholder for an icon
        "Summary"
    )
}

//@Composable
//fun BottomNavigationBar(navController: NavHostController) {
//    val items = listOf(
//        BottomNavItem.TrucksDetail,
//        BottomNavItem.Dispatch,
//        BottomNavItem.Trucks,
//        BottomNavItem.ActiveJourneys,
//        BottomNavItem.JourneySummary // Added new item to the list
//    )
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
//
//    NavigationBar {
//        items.forEach { item ->
//            val isSelected = currentRoute == item.route.toString()
//            NavigationBarItem(
//                icon = {
//                    Icon(
//                        // Load ImageVector using the resource ID
//                        ImageVector.vectorResource(id = item.iconResourceId),
//                        contentDescription = item.label,
//                        tint = if(isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceTint
//                    )
//                },
//                label = { Text(item.label) },
//                selected = currentRoute == item.route.toString(),
//                onClick = {
//                    navController.navigate(item.route) {
//                        // Avoid building up a large stack of destinations on the back stack as users select items
//                        popUpTo(navController.graph.startDestinationRoute!!) {
//                            saveState = true
//                        }
//                        // Restore state when reselecting a previously selected item
//                        restoreState = true
//                        // Launch as a single top level destination to avoid duplicates
//                        launchSingleTop = true
//                    }
//                }
//            )
//        }
//    }
//}