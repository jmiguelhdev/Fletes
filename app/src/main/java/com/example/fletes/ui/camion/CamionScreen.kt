package com.example.fletes.ui.camion

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fletes.R
import com.example.fletes.data.room.Camion
import java.time.LocalDate

@Composable
fun CamionScreen(viewModel: CamionViewModel) {
    val camiones = viewModel.camiones.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopBar(
                onInsertCamion = { viewModel.insertCamion() },
                onDeleteAllCamions = { viewModel.deleteAllCamiones() },
                onBack = {}
            )
        },
        content = { paddingValues ->
            ListOfCamionesScreen(
                camiones = camiones.value,
                ondeleteCamion = { viewModel.deleteCamion(it) },
                modifier = Modifier
                    .padding(paddingValues)
                )

        },
        bottomBar = {

        }
    )
}




@Composable
fun ListOfCamionesScreen(
    camiones: List<Camion>,
    ondeleteCamion: (Int) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(camiones) { camion ->
                CamionCard(
                    camion =camion ,
                    ondeleteCamion = ondeleteCamion,
                    modifier= Modifier)
            }
        }
    }
}

@Composable
fun CamionCard(
    camion: Camion,
    ondeleteCamion: (Int) -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp).weight(1f)
            ) {
                Text(
                    text = "Chofer: ${camion.choferName}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = "DNI: ${camion.choferDni}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = "Patente Tractor: ${camion.patenteTractor}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = "Patente Jaula: ${camion.patenteJaula}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 4.dp, end = 8.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(text = "ID: ${camion.id}", style = MaterialTheme.typography.bodyMedium)
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete_outline_24),
                     contentDescription = "Delete",
                     modifier = Modifier.clickable { ondeleteCamion(camion.id) }
                 )
             }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onInsertCamion: () -> Unit,
    onDeleteAllCamions: () -> Unit,
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
                onInsertCamion()
                Log.d("CamionViewModel", "Camion inserted")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_24),
                    contentDescription = "Añadir"
                )
            }
            IconButton(onClick = {
                onDeleteAllCamions()
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
fun ListOfCamionesScreenPreview() {
    val camiones = listOf(
        Camion(
            id = 1,
            createdAt = LocalDate.now(),
            choferName = "Chofer 1",
            choferDni = 12345678,
            patenteTractor = "PATENTE1",
            patenteJaula = "JAULA1",
            kmService = 10000
        ),
        Camion(
            id = 2,
            createdAt = LocalDate.now(),
            choferName = "Chofer 1",
            choferDni = 12345678,
            patenteTractor = "PATENTE1",
            patenteJaula = "JAULA1",
            kmService = 10000
        ),
        Camion(
            id = 13,
            createdAt = LocalDate.now(),
            choferName = "Chofer 1",
            choferDni = 12345678,
            patenteTractor = "PATENTE1",
            patenteJaula = "JAULA1",
            kmService = 10000
        ),
    )

    Column {
        ListOfCamionesScreen(
            camiones = camiones,
            ondeleteCamion = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar(
        onInsertCamion = { },
        onDeleteAllCamions = { },
        onBack = { }
    )
}
