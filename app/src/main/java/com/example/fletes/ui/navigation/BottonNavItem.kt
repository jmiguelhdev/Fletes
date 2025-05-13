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
        TrucksDetailScreenRoute,
         R.drawable.ic_add_24,
        "Details"
    )

    object Dispatch : BottomNavItem(
        DispatchScreenRoute,
        R.drawable.ic_location_pin_24,
        "Dispatch"
    )

    object Trucks : BottomNavItem(
        TruckScreenRoute,
        R.drawable.icl_shipping_24,
        "Trucks"
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.TrucksDetail,
        BottomNavItem.Dispatch,
        BottomNavItem.Trucks
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentRoute == item.route.toString()
            NavigationBarItem(
                icon = {
                    Icon(
                        // Load ImageVector using the resource ID
                        ImageVector.vectorResource(id = item.iconResourceId),
                        contentDescription = item.label,
                        tint = if(isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceTint
                    )
                },
                label = { Text(item.label) },
                selected = currentRoute == item.route.toString(),
                onClick = {
                    navController.navigate(item.route) {
                        // Avoid building up a large stack of destinations on the back stack as users select items
                        popUpTo(navController.graph.startDestinationRoute!!) {
                            saveState = true
                        }
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                        // Launch as a single top level destination to avoid duplicates
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}