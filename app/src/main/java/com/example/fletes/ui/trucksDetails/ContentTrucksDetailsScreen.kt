package com.example.fletes.ui.trucksDetails

import android.content.res.Configuration
import android.icu.text.NumberFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.example.fletes.R
import com.example.fletes.data.model.DecimalTextFieldData
import com.example.fletes.data.model.truckJourneyData.TruckJourneyData
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.Destino
import com.example.fletes.ui.dispatch.DecimalTextField
import com.example.fletes.ui.dispatch.DeleteDestinoAlertDialog
import com.example.fletes.ui.theme.FletesTheme
import java.time.LocalDate
import java.util.Locale

@Composable
fun ContentTrucksDetailsScreen(
    modifier: Modifier = Modifier,
    activeDispatch: List<Destino> = emptyList(),
    showDeleteDialog: Boolean = false,
    onDismissRequestDelete: () -> Unit,
    onConfirmDelete: (Destino) -> Unit = {},
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    showUpdateDialog: Boolean = false,
    onDismissRequestUpdate: () -> Unit,
    onConfirmUpdate: (Destino) -> Unit = { },
    value: String,
    onValueChange: (String) -> Unit = {},
    errorMessage: String?,
    listCamiones: List<Camion>,
    onClickChip: (camion: Camion) -> Unit,
    camion: Camion,
    truckJourneyData: TruckJourneyData,
    onClickSaveOrUpdateTrip: (destinoId: Int) -> Unit = {},
) {
    ActiveDispatch(
        activeDispatch = activeDispatch,
        onDeleteClick = onDeleteClick,
        showDeleteDialog = showDeleteDialog,
        onDismissRequestDelete = onDismissRequestDelete,
        onConfirmDelete = onConfirmDelete,
        showUpdateDialog = showUpdateDialog,
        onDismissRequestUpdate = onDismissRequestUpdate,
        onConfirmUpdate = onConfirmUpdate,
        onEditClick = onEditClick,
        value = value,
        onValueChange = onValueChange,
        errorMessage = errorMessage,
        modifier = modifier.fillMaxWidth(),
        listCamiones = listCamiones,
        onClickChip = onClickChip,
        camion = camion,
        truckJourneyData = truckJourneyData,
        onClickSaveOrUpdateTrip = onClickSaveOrUpdateTrip
    )
}


@Composable
fun ActiveDispatch(
    modifier: Modifier = Modifier,
    activeDispatch: List<Destino>,
    showDeleteDialog: Boolean = false,
    onDismissRequestDelete: () -> Unit,
    onConfirmDelete: (Destino) -> Unit = {},
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    showUpdateDialog: Boolean = false,
    onDismissRequestUpdate: () -> Unit,
    onConfirmUpdate: (Destino) -> Unit = {},
    value: String,
    onValueChange: (String) -> Unit = {},
    errorMessage: String?,
    listCamiones: List<Camion>,
    onClickChip: (camion: Camion) -> Unit,
    camion: Camion,
    truckJourneyData: TruckJourneyData,
    onClickSaveOrUpdateTrip: (destinoId: Int) -> Unit = {},
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        item {
            CamionChipRow(
                camionList = listCamiones
            ){
                onClickChip(it)
            }
        }

        items(
            items = activeDispatch,
            key = { destino -> destino.id }
        ) { destino ->
            DispatchCard(
                dispatch = destino,
                onDeleteClick = onDeleteClick,
                onEditClick = onEditClick,
                modifier = Modifier,
                camion = camion,
                truckJourneyData = truckJourneyData,
                onClickSaveOrUpdateTrip = onClickSaveOrUpdateTrip

            )
            if (showDeleteDialog) {
                DeleteDestinoAlertDialog(
                    destino = destino,
                    onDismissRequestDelete = onDismissRequestDelete,
                    onConfirmDelete = onConfirmDelete,
                )
            }
            if (showUpdateDialog) {
                UpdateDestinoAlertDialog(
                    destino = destino,
                    value = value,
                    onValueChange = onValueChange,
                    onDismissRequest = onDismissRequestUpdate,
                    onConfirm = onConfirmUpdate,
                    errorMessage = errorMessage,
                    modifier = modifier,
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DispatchCard(
    modifier: Modifier = Modifier,
    dispatch: Destino,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    camion: Camion,
    truckJourneyData: TruckJourneyData,
    onClickSaveOrUpdateTrip: (destinoId: Int) -> Unit = {},
) {
    var isExpanded by remember { mutableStateOf(true) } //cambiar a false para probar

    if (dispatch.isActive) {
        Card(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .animateContentSize() // Smooth animation for expansion
                .clickable { isExpanded = !isExpanded } // Toggle expansion on click
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header (Always Visible)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = dispatch.createdAt.toString())
                    Text(text = dispatch.comisionista)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { isExpanded = !isExpanded }) {
                            Icon(
                                painter = if (isExpanded) painterResource(R.drawable.ic_expand_less_24) else painterResource(
                                    R.drawable.ic_expand_more_24
                                ),
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                            )
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete_24),
                            contentDescription = "delete icon",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable { onDeleteClick() }
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.edit_edit_24),
                            contentDescription = "edit icon",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable { onEditClick() }
                        )
                    }
                }
                // Expanded Content (Conditional Visibility)
                AnimatedVisibility(visible = isExpanded) {
                    Column {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            color = Color.Black,
                            thickness = 2.dp
                        )
                        Text(text = "Commission agent Name: ${dispatch.comisionista}")
                        Spacer(modifier = Modifier.padding(vertical = 4.dp))
                        HorizontalDivider(modifier = Modifier, thickness = 1.dp)

                        val format: NumberFormat = NumberFormat
                            .getCurrencyInstance(Locale.getDefault())
                        val formattedNumber = format.format(dispatch.despacho)
                        Spacer(modifier = Modifier.padding(vertical = 4.dp))
                        Text(text = "Dispatch amount: $formattedNumber")
                        Spacer(modifier = Modifier.padding(vertical = 4.dp))
                        HorizontalDivider(modifier = Modifier, thickness = 1.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_location_pin_24),
                                contentDescription = "Map icon"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = dispatch.localidad)
                        }
                        Spacer(modifier = Modifier.padding(vertical = 2.dp))
                        HorizontalDivider(modifier = Modifier, thickness = 1.dp)
                        JourneyCard(
                            modifier = Modifier,
                            camion = camion,
                            truckJourneyData = truckJourneyData,
                        )
                        SaveOrUpdateTripButton(
                            modifier = Modifier,
                            destinationId = dispatch.id,
                            isActive = dispatch.isActive,
                            onClickSaveOrUpdateTrip = onClickSaveOrUpdateTrip
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SaveOrUpdateTripButton(
    modifier: Modifier = Modifier,
    destinationId: Int,
    isActive: Boolean, //implementar
    onClickSaveOrUpdateTrip: (destinoId: Int) -> Unit = {},
) {
    OutlinedButton(
        onClick = { onClickSaveOrUpdateTrip(destinationId) },
        modifier = modifier.fillMaxWidth(),
        enabled = isActive
    ) {
        Text(text = "Save or Update Trip")
    }
    
}

@Composable
fun JourneyCard(
    modifier: Modifier = Modifier,
    camion: Camion,
    truckJourneyData: TruckJourneyData,
) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Registro de viaje Chofer: ")
            Text(text = camion.choferName)
        }
        HorizontalDivider(thickness = DividerDefaults.Thickness)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(4.dp)) {
                    DecimalTextField(
                        value = truckJourneyData.kmCargaData.value,
                        onValueChange = truckJourneyData.kmCargaData.onValueChange,
                        label = "km carga",
                        errorMessage = truckJourneyData.kmCargaData.errorMessage,
                    )
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(4.dp)) {
                    DecimalTextField(
                        value = truckJourneyData.kmDescargaData.value,
                        onValueChange = truckJourneyData.kmDescargaData.onValueChange,
                        label = "km descarga",
                        errorMessage = truckJourneyData.kmDescargaData.errorMessage,
                    )
                }
            }
        }
        HorizontalDivider(thickness = DividerDefaults.Thickness)
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(4.dp)) {
                    DecimalTextField(
                        value = truckJourneyData.kmSurtidorData.value,
                        onValueChange = truckJourneyData.kmSurtidorData.onValueChange,
                        label = "km surtidor",
                        errorMessage = truckJourneyData.kmDescargaData.errorMessage,
                    )
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(4.dp)) {
                    DecimalTextField(
                        value = truckJourneyData.litrosData.value,
                        onValueChange = truckJourneyData.litrosData.onValueChange,
                        label = "litros surtidos",
                        errorMessage = truckJourneyData.litrosData.errorMessage,
                    )
                }
            }
        }
        HorizontalDivider(thickness = DividerDefaults.Thickness)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun JourneyCardPreview() {
    val sampleCamion = Camion(
        id = 1,
        createdAt = LocalDate.now(),
        choferName = "Miguel",
        choferDni = 29673971,
        patenteTractor = "Ad123dc",
        patenteJaula = "Cd456Fd",
        isActive = true
    )
    // Creamos un objeto de ejemplo de TruckJourneyData
    // Cada campo (kmCargaData, kmDescargaData, etc.) ahora es un TextFieldData
    val sampleTruckJourneyData = TruckJourneyData(
        camionId = 0,
        kmCargaData = DecimalTextFieldData(
            label = "km carga",
            value = "123.0",
            onValueChange = { /* Aquí puedes agregar lógica de manejo de cambios si es necesario */ },
            errorMessage = ""
        ),
        kmDescargaData = DecimalTextFieldData(
            label = "km descarga",
            value = "231.0",
            onValueChange = { /* Aquí puedes agregar lógica de manejo de cambios si es necesario */ },
            errorMessage = ""
        ),
        kmSurtidorData = DecimalTextFieldData(
            label = "km surtidor",
            value = "100.0",
            onValueChange = { /* Aquí puedes agregar lógica de manejo de cambios si es necesario */ },
            errorMessage = ""
        ),
        litrosData = DecimalTextFieldData(
            label = "litros surtidos",
            value = "50.0",
            onValueChange = { /* Aquí puedes agregar lógica de manejo de cambios si es necesario */ },
            errorMessage = ""
        ),
        isActive = false
    )

    FletesTheme {
        JourneyCard(
            camion = sampleCamion,
            truckJourneyData = sampleTruckJourneyData,
        )
    }
}

@Composable
fun CamionChipRow(
    camionList: List<Camion>,
    onClick: (camion: Camion) -> Unit = {}
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            camionList
        ) {
            CamionChip(
                camion = it,
                isChipActive = it.isActive,
                onClick = onClick
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CamionChipRowPrev() {
    val camionList = listOf(
        Camion(
            id = 1,
            choferName = "Juan Perez",
            createdAt = LocalDate.now(),
            choferDni = 29384756,
            patenteTractor = "ad123fg",
            patenteJaula = "df213fg",
            isActive = true,
        ),
        Camion(
            id = 2,
            choferName = "Roberto Carlos",
            createdAt = LocalDate.now(),
            choferDni = 29384756,
            patenteTractor = "ad123fg",
            patenteJaula = "df213fg",
            isActive = true,
        )
    )
    CamionChipRow(camionList = camionList)
}

@Composable
fun CamionChip(
    camion: Camion,
    isChipActive: Boolean = true,
    onClick: (camion: Camion) -> Unit = {}
) {


    ElevatedAssistChip(
        onClick = {
            onClick(camion)
        },
        label = { Text("Chofer: ${camion.choferName}") },
        modifier = Modifier
            .padding(4.dp)
            .wrapContentWidth(),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (isChipActive) Color.Green else Color.Red,
            labelColor = Color.White,
            leadingIconContentColor = Color.White, // Color para el icono si lo hubiera
            trailingIconContentColor = Color.White,
        )
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    wallpaper = Wallpapers.NONE
)
@Composable
fun PreviewCamionChip() {
    val camion = Camion(
        id = 1,
        choferName = "Juan Perez",
        createdAt = LocalDate.now(),
        choferDni = 29384756,
        patenteTractor = "ad123fg",
        patenteJaula = "df213fg",
        isActive = true,
    )
    CamionChip(camion = camion)
}

@Composable
fun UpdateDestinoAlertDialog(
    destino: Destino,
    onDismissRequest: () -> Unit,
    onConfirm: (destino: Destino) -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = { onConfirm(destino) }
            ) {
                Text(text = "Confirm Update")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(text = "Cancelar")
            }
        },
        title = {
            Text(text = "Editando Destino: ${destino.localidad}")
        },
        text = {
            Column {
                Text(text = "Fecha: ${destino.createdAt}")
                Text(text = "Despacho: ${destino.despacho}")
                Text(text = "Comisionista: ${destino.comisionista}")
                Text(text = "Destino: ${destino.localidad}")
                Text(text = "Ingrese nuevo valor de despacho")
                HorizontalDivider(thickness = 2.dp)
                DecimalTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = "Despacho",
                    errorMessage = errorMessage,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }, modifier = Modifier.wrapContentHeight()
    )
}

@Preview(showBackground = true)
@Composable
fun UpdateDestinoAlertDialogPreview(modifier: Modifier = Modifier) {
    FletesTheme {
        UpdateDestinoAlertDialog(
            destino = Destino(
                id = 1,
                createdAt = LocalDate.now(),
                comisionista = "Miguel",
                despacho = 10_000_000.0,
                localidad = "Buenos Aires",
                isActive = true
            ),
            onDismissRequest = { },
            onConfirm = { },
            value = "10000000.0",
            onValueChange = { },
            errorMessage = null,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DispatchCardPrev(modifier: Modifier = Modifier) {
    val sampleCamion = Camion(
        id = 1,
        createdAt = LocalDate.now(),
        choferName = "Miguel",
        choferDni = 29673971,
        patenteTractor = "Ad123dc",
        patenteJaula = "Cd456Fd",
        isActive = true
    )
    // Creamos un objeto de ejemplo de TruckJourneyData
    // Cada campo (kmCargaData, kmDescargaData, etc.) ahora es un TextFieldData
    val sampleTruckJourneyData = TruckJourneyData(
        camionId = 0,
        kmCargaData = DecimalTextFieldData(
            label = "km carga",
            value = "123.0",
            onValueChange = { /* Aquí puedes agregar lógica de manejo de cambios si es necesario */ },
            errorMessage = ""
        ),
        kmDescargaData = DecimalTextFieldData(
            label = "km descarga",
            value = "231.0",
            onValueChange = { /* Aquí puedes agregar lógica de manejo de cambios si es necesario */ },
            errorMessage = ""
        ),
        kmSurtidorData = DecimalTextFieldData(
            label = "km surtidor",
            value = "100.0",
            onValueChange = { /* Aquí puedes agregar lógica de manejo de cambios si es necesario */ },
            errorMessage = ""
        ),
        litrosData = DecimalTextFieldData(
            label = "litros surtidos",
            value = "50.0",
            onValueChange = { /* Aquí puedes agregar lógica de manejo de cambios si es necesario */ },
            errorMessage = ""
        ),
        isActive = false
    )
    FletesTheme {
        DispatchCard(
            modifier = modifier,
            dispatch = Destino(
                id = 1,
                createdAt = LocalDate.now(),
                comisionista = "Miguel",
                despacho = 10_000_000.0,
                localidad = "Buenos Aires",
                isActive = true
            ),
            onDeleteClick = {},
            onEditClick = {},
            camion = sampleCamion,
            truckJourneyData = sampleTruckJourneyData,
        )
    }
}



