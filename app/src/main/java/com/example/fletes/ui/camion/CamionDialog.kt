package com.example.fletes.ui.camion

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.fletes.data.room.Camion
import com.example.fletes.ui.camion.components.MyInsertTruckDialogContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamionDialog(
    camionUiState: CamionUiState,
    camionViewModel: CamionViewModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Insert Truck") },
        text = {
            MyInsertTruckDialogContent(
                input1 = camionUiState.choferName,
                label1 = "Driver Name",
                onValueChange1 = { camionViewModel.onChoferNameValueChange(it) },

                input2 = camionUiState.choferDni.toString(),
                label2 = "Driver License",
                isValid2 = camionUiState.isValidDni,
                error2msg = camionUiState.driverDniErrorMessage.toString(),
                onValueChange2 = { camionViewModel.onChoferDniValueChange(it) },

                input3 = camionUiState.patenteTractor,
                label3 = "Truck License Plate",
                isValid3 = camionUiState.isValidPatenteTractor,
                error3msg = camionUiState.patenteTractorErrorMessage,
                onValueChange3 = { camionViewModel.onPatenteTractorValueChange(it) },

                input4 = camionUiState.patenteJaula,
                label4 = "Trailer License Plate",
                isValid4 = camionUiState.isValidPatenteJaula,
                error4msg = camionUiState.patenteJaulaErrorMessage,
                onValueChange4 = { camionViewModel.onPatenteJaulaValueChange(it) }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamionUpdateDialog(
    camion: Camion,
    camionUiState: CamionUiState,
    camionViewModel: CamionViewModel,
    onDismiss: () -> Unit,
    onConfirm: (id: Int) -> Unit
) {

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        onDismissRequest = onDismiss,
        title = { Text("Edit Truck") },
        text = {
            MyInsertTruckDialogContent(
                input1 = camionUiState.choferName,
                label1 = "Driver Name",
                onValueChange1 = { camionViewModel.onChoferNameValueChange(it) },

                input2 = camionUiState.choferDni.toString(),
                label2 = "Driver License",
                isValid2 = camionUiState.isValidDni,
                error2msg = camionUiState.driverDniErrorMessage.toString(),
                onValueChange2 = { camionViewModel.onChoferDniValueChange(it) },

                input3 = camionUiState.patenteTractor,
                label3 = "Truck License Plate",
                isValid3 = camionUiState.isValidPatenteTractor,
                error3msg = camionUiState.patenteTractorErrorMessage,
                onValueChange3 = { camionViewModel.onPatenteTractorValueChange(it) },

                input4 = camionUiState.patenteJaula,
                label4 = "Trailer License Plate",
                isValid4 = camionUiState.isValidPatenteJaula,
                error4msg = camionUiState.patenteJaulaErrorMessage,
                onValueChange4 = { camionViewModel.onPatenteJaulaValueChange(it) }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(camion.id)
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
