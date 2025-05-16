package com.example.fletes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.fletes.ui.navigation.MyNavHost
import com.example.fletes.ui.theme.FletesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            FletesTheme {
                MyNavHost(
                    navController = navController
                )
            }
        }
    }
}

