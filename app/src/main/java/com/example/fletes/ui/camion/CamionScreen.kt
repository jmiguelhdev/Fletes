package com.example.fletes.ui.camion

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CamionScreen(viewModel: CamionViewModel) {
    val camiones = viewModel.camiones.collectAsStateWithLifecycle()
    Scaffold { paddingValues ->
        LazyColumn {
            items(camiones.value.size) { index ->
                Text(text = camiones.value[index].toString())
            }
        }
        InserCamionScreen(paddingValues) {
            viewModel.insertCamion()
        }
    }
}

@Composable
fun InserCamionScreen(paddingValues: PaddingValues, onInsertCamion: () -> Unit) {

    Button(
        onClick = {
            onInsertCamion()
            Log.d("CamionViewModel", "Camion inserted")
        },
        modifier = Modifier.padding(paddingValues)
    ) {
        Text(text = "Insertar Camion")
    }

}