package com.example.fletes.ui.screenTruck.components

import android.util.Log
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import com.example.fletes.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TruckTopAppBar(
    onInsertTruck: () -> Unit,
    onDeleteAllTrucks: () -> Unit,
    onBack: () -> Unit, // Kept for now, but Menu icon will take precedence in navigationIcon
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    CenterAlignedTopAppBar(
        title = { Text("Camiones") },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Open Navigation Drawer"
                )
            }
        },
        actions = {
            // The onBack functionality is not used here if navigationIcon is for the drawer menu.
            // If a separate back button is needed in actions, it would be added here.
            // For now, we keep the existing actions.
            IconButton(onClick = {
                onInsertTruck()
                Log.d("CamionViewModel", "Camion inserted")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_24),
                    contentDescription = "Añadir"
                )
            }
            IconButton(onClick = {
                onDeleteAllTrucks()
                Log.d("CamionViewModel", "Deleted all camiones")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete_24),
                    contentDescription = "Borrar"
                )
            }
        })
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    // Preview needs to be updated to provide DrawerState and CoroutineScope
    // For simplicity, we'll use remember versions here.
    val previewScope = androidx.compose.runtime.rememberCoroutineScope()
    val previewDrawerState = androidx.compose.material3.rememberDrawerState(androidx.compose.material3.DrawerValue.Closed)
    TruckTopAppBar(
        onInsertTruck = { },
        onDeleteAllTrucks = { },
        onBack = { },
        drawerState = previewDrawerState,
        scope = previewScope
    )
}