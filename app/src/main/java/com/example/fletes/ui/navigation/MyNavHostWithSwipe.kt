package com.example.fletes.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fletes.data.room.Destino
import com.example.fletes.ui.screenActiveDispatch.ActiveDispatchDetailsScreen
import com.example.fletes.ui.screenDispatch.NewDispatchScreen
import com.example.fletes.ui.screenDispatch.NewDispatchViewModel
import com.example.fletes.ui.screenTruck.TruckScreen
import com.example.fletes.ui.screenTruck.TruckViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun MyNavHostWithSwipe(
    navController: NavHostController,
) {
    val newDispatchViewModel: NewDispatchViewModel = koinViewModel()
    val truckViewModel: TruckViewModel = koinViewModel()
    val activeTrucks by truckViewModel.camiones.collectAsState(emptyList())
    val unActiveDestinations by newDispatchViewModel.unActiveDestinations.collectAsState(emptyList())
    val initialActiveDipatch = listOf(
        Destino(
            id = 1,
            comisionista = "",
            despacho = 0.0,
            localidad = "",
            isActive = true
        )
    )
    val activeDispatch by newDispatchViewModel.activeDispatch.collectAsState(initialActiveDipatch)

    LaunchedEffect(key1 = true) {
        truckViewModel.loadCamiones()
    }

    val bottomNavItems = listOf(
        BottomNavItem.TrucksDetail,
        BottomNavItem.Dispatch,
        BottomNavItem.Trucks
    )
    val pagerState = rememberPagerState(initialPage = 0) {
        bottomNavItems.size
    }
    val coroutineScope = rememberCoroutineScope()

    // Synchronize Pager swipes with Bottom Navigation selection
    LaunchedEffect(pagerState.currentPage) {
        val currentRoute = bottomNavItems[pagerState.currentPage].route
        // You might need to find the corresponding destination in the NavController's graph
        // and navigate to it to update the back stack and highlight the correct item
        // in the BottomNavigationBar. This is a simplified approach.
        // A more robust solution might involve managing the NavController's state based on pager state.
        // For this example, we'll assume a direct mapping for simplicity.
        val navDestination = navController.graph.find { it.route == currentRoute.toString() }
        if (navDestination != null) {
            navController.navigate(currentRoute) {
                popUpTo(navController.graph.startDestinationRoute!!) {
                    saveState = true
                }
                restoreState = true
                launchSingleTop = true
            }
        }
    }


    Scaffold(
        bottomBar = {
            BottomNavigationBarWithPager(
                navController = navController,
                items = bottomNavItems,
                onItemClick = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            // Each page represents a destination
            when (bottomNavItems[page].route) {
                TrucksDetailScreenRoute -> {
                    if (activeTrucks.isNotEmpty()) {
                        ActiveDispatchDetailsScreen(
                            newDispatchViewModel = newDispatchViewModel,
                            truckViewModel = truckViewModel,
                            onClickFab = {
                                // Navigation within a pager page might need a separate NavHost
                                // or a different navigation strategy. For this example,
                                // we'll keep the existing navigation calls, but be aware
                                // this might need adjustment for complex flows.
                                navController.navigate(DispatchScreenRoute)
                            },
                            onClickAction = {
                                navController.navigate(TruckScreenRoute)
                            },
                            alltrucks = activeTrucks,
                            activeDispatch = activeDispatch,
                            unActiveDestinations = unActiveDestinations
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                DispatchScreenRoute -> {
                    NewDispatchScreen(
                        viewModel = newDispatchViewModel,
                    ) {
                        navController.popBackStack()
                    }
                }

                TruckScreenRoute -> {
                    TruckScreen(
                        truckViewModel = truckViewModel,
                    ) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBarWithPager(
    navController: NavHostController,
    items: List<BottomNavItem>,
    onItemClick: (Int) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        ImageVector.vectorResource(id = item.iconResourceId),
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                selected = currentRoute == item.route.toString(),
                onClick = { onItemClick(index) } // Call onItemClick to change pager page
            )
        }
    }
}