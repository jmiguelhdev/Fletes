package com.example.fletes.ui.camion

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fletes.R
import com.example.fletes.data.room.Camion
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun CamionScreen(
    camionViewModel: CamionViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {
    val camiones = camionViewModel.camiones.collectAsStateWithLifecycle()
    val uiState = camionViewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopBar(
                onInsertCamion = { camionViewModel.showDialog() },
                onDeleteAllCamions = { camionViewModel.deleteAllCamiones() },
                onBack = onNavBack
            )
        },
        content = { paddingValues ->
            ListOfCamionesScreen(
                camiones = camiones.value,
                ondeleteCamion = { camionViewModel.deleteCamion(it) },
                onEditCamion = {
                    camionViewModel.onShowEditDialog(it)
                },
                modifier = Modifier
                    .padding(paddingValues)

            )
            if (uiState.value.showInsertDialog) {
                CamionDialog(
                    camionUiState = uiState.value,
                    camionViewModel = camionViewModel,
                    onDismiss = { camionViewModel.hideDialog() },
                    onConfirm = { camionViewModel.insertCamion() }
                )
            }
            if (uiState.value.showEditDialog) {
                CamionUpdateDialog(
                    camion = camiones.value.first(),
                    camionUiState = uiState.value,
                    camionViewModel = camionViewModel,
                    onDismiss = { camionViewModel.hideDialog() },
                    onConfirm = {
                        camionViewModel.updateCamion(id = it) }
                )
            }
        },
        bottomBar = {

        },

    )
}


@Composable
fun ListOfCamionesScreen(
    camiones: List<Camion>,
    ondeleteCamion: (Int) -> Unit,
    onEditCamion: (Int) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(modifier = Modifier) {
            items(camiones) { camion ->
                CamionCard(
                    camion = camion,
                    onDeleteCamion = ondeleteCamion,
                    onEditCamion = onEditCamion,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun CamionCard(
    camion: Camion,
    onDeleteCamion: (Int) -> Unit,
    onEditCamion: (Int) -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .padding(2.dp)
            .fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CamionDetailsColumn(camion = camion, modifier = Modifier.weight(1f))
                InteractColum(
                    camion = camion,
                    onDeleteCamion = onDeleteCamion,
                    onEditCamion = onEditCamion
                )
            }
        }
    }
}

@Composable
private fun InteractColum(
    camion: Camion,
    onDeleteCamion: (Int) -> Unit,
    onEditCamion: (Int) -> Unit
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "ID: ${camion.id}", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.padding(vertical = 8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_delete_outline_24),
            contentDescription = "Delete",
            modifier = Modifier.clickable { onDeleteCamion(camion.id) }
        )
        Spacer(Modifier.padding(vertical = 8.dp))
        Icon(
            painter = painterResource(R.drawable.edit_edit_24),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "Edit",
            modifier = Modifier
                .clickable {
                    Log.d("CamionViewModel", "Edit camion ${camion.id}")
                    onEditCamion(camion.id)
                }
        )
    }
}


@Composable
fun CamionDetailsColumn(camion: Camion, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DetailRow(
            label = "Chofer",
            value = camion.choferName,
            style = MaterialTheme.typography.titleMedium
        )
        DetailRow(label = "DNI", value = camion.choferDni.toString())
        DetailRow(label = "Patente Tractor", value = camion.patenteTractor)
        DetailRow(label = "Patente Jaula", value = camion.patenteJaula)
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$label: $value",
            style = style
        )
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
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
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
fun TopBarPreview() {
    TopBar(
        onInsertCamion = { },
        onDeleteAllCamions = { },
        onBack = { }
    )
}

@Preview(showBackground = true)
@Composable
fun CamionCardPreview() {
    val mockCamion = Camion(
        id = 1,
        choferName = "John Doe",
        choferDni = 12345678,
        patenteTractor = "AB123CD",
        patenteJaula = "EF456GH",
        createdAt = LocalDate.now(),
        kmService = 20000
    )
    CamionCard(
        camion = mockCamion,
        onDeleteCamion = {},
        onEditCamion = {},
        modifier = Modifier
    )
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
            onEditCamion = {},
            modifier = Modifier
        )
    }
}


@Preview(showBackground = true)
@Composable
fun InteractColumPreview() {
    val mockCamion = Camion(
        id = 1,
        createdAt = LocalDate.now(),
        choferName = "Jonh Doe",
        choferDni = 123456789,
        patenteTractor = "AA123BB",
        patenteJaula = "CC456DD",
        kmService = 20000,
    )
    InteractColum(camion = mockCamion, onDeleteCamion = {}, onEditCamion = {})
}
