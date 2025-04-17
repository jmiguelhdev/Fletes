package com.example.fletes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fletes.ui.navigation.MyNavHost
import com.example.fletes.ui.navigation.Screen.DestinationScreenRoute
import com.example.fletes.ui.navigation.Screen.TruckScreenRoute
import com.example.fletes.ui.navigation.ScreenSRoutes
import com.example.fletes.ui.theme.FletesTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FletesTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val pagerState = rememberPagerState(
        pageCount = { 2 }
    )

    val scope = rememberCoroutineScope()
    val items = listOf(
        ScreenSRoutes.Screen1,
        ScreenSRoutes.Screen2
    )
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val curentDestination = navBackStackEntry.value?.destination
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.icon),
                                contentDescription = null
                            )
                        },
                        selected = curentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        onClick = {
                            scope.launch {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }

                                pagerState.animateScrollToPage(index)
                            }
                        },
                        modifier = Modifier,
                        label = { Text(screen.title) },
                        alwaysShowLabel = false,
                    )
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues)
        ) { page ->
            val startDestination = when (page) {
                0 -> ScreenSRoutes.Screen1.route
                1 -> ScreenSRoutes.Screen2.route
                else -> ScreenSRoutes.Screen1.route
            }
            MyNavHost(
                navController = navController,
                startDestination = startDestination,
                paddingValues = paddingValues
            )
        }
    }
}