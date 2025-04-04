package com.example.fletes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.fletes.ui.camion.CamionViewModel
import com.example.fletes.ui.navigation.MyNavHost
import com.example.fletes.ui.theme.FletesTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val camionViewModel: CamionViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FletesTheme {
                val navController = rememberNavController()
                MyNavHost(navController = navController, camionViewModel)
            }
        }
    }
}

