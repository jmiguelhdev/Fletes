package com.example.fletes.ui.camion

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fletes.data.room.Camion
import java.time.LocalDate

@Composable
fun CamionScreen(viewModel: CamionViewModel) {
    val camiones = viewModel.camiones.collectAsStateWithLifecycle()
    Scaffold(
        content = {
            ListOfCamionesScreen(
                camiones = camiones.value,
                onInsertCamion = { viewModel.insertCamion() },
                onDeleteAllCamions = {viewModel.deleteAllCamiones()},
                modifier = Modifier.padding(it)
            )
        }
    )
}


@Composable
fun ListOfCamionesScreen(
    camiones: List<Camion>,
    onInsertCamion: () -> Unit,
    onDeleteAllCamions: () -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(camiones) { camion ->
                CamionCard(camion = camion, modifier = modifier)
            }
        }
        Row {
            Button(
                onClick = {
                    onInsertCamion()
                    Log.d("CamionViewModel", "Camion inserted")
                },
                modifier = modifier

            ) {
                Text(text = "Insertar Camion")
            }
            Button(
                onClick = {
                    onDeleteAllCamions()
                    Log.d("CamionViewModel", "Deleted all camiones")
                },
                modifier = modifier

            ) {
                Text(text = "Borrar Todos")
            }
        }
    }
}

@Composable
fun CamionCard(camion: Camion, modifier: Modifier) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
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

@Preview(showBackground = true)
@Composable
fun ListOfCamionesScreenPreview() {
    val camiones = listOf(
        Camion(
            id = 1,
            createdAt = LocalDate.now(),
            choferName = "Chofer 1",
            choferPrice = 100.0,
            patenteTractor = "PATENTE1",
            patenteJaula = "JAULA1",
            kmService = 10000
        ),
        Camion(
            id = 2,
            createdAt = LocalDate.now(),
            choferName = "Chofer 1",
            choferPrice = 100.0,
            patenteTractor = "PATENTE1",
            patenteJaula = "JAULA1",
            kmService = 10000
        ),
        Camion(
            id = 13,
            createdAt = LocalDate.now(),
            choferName = "Chofer 1",
            choferPrice = 100.0,
            patenteTractor = "PATENTE1",
            patenteJaula = "JAULA1",
            kmService = 10000
        ),
    )

    Column {
        ListOfCamionesScreen(
            camiones = camiones,
            onInsertCamion = { },
            onDeleteAllCamions = {},
            modifier = Modifier.padding(8.dp)
        )
    }
}
