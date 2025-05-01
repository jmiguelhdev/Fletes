package com.example.fletes.ui.screenTruck.components

import android.util.Log
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fletes.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TruckTopAppBar(
    onInsertTruck: () -> Unit,
    onDeleteAllTrucks: () -> Unit,
    onBack: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text("Camiones") },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back_24),
                    contentDescription = "Atras"
                )
            }
        },
        actions = {
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
    TruckTopAppBar(
        onInsertTruck = { },
        onDeleteAllTrucks = { },
        onBack = { }
    )
}