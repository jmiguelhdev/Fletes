package com.example.fletes.ui.camion

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fletes.data.room.Camion

@Composable
fun CamionScreen(viewModel: CamionViewModel) {
    val camiones = viewModel.camiones.collectAsStateWithLifecycle()
    Scaffold(
        content = {
            ListOfCamionesScreen(
                camiones = camiones.value,
                onInsertCamion = { viewModel.insertCamion() },
                modifier = Modifier.padding(it)
            )
        }
    )
}


@Composable
fun ListOfCamionesScreen(
    camiones: List<Camion>,
    onInsertCamion: () -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        LazyColumn(modifier = modifier) {
            items(camiones.size) { index ->
                CamionCard(camion = camiones[index])
            }
        }
    }
    Button(
        onClick = {
            onInsertCamion()
            Log.d("CamionViewModel", "Camion inserted")
        },
        modifier = Modifier
    ) {
        Text(text = "Insertar Camion")
    }

}

@Composable
fun CamionCard(camion: Camion) {
    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "ID: ${camion.id}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Chofer: ${camion.choferName}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Patente Tractor: ${camion.patenteTractor}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Patente Jaula: ${camion.patenteJaula}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
