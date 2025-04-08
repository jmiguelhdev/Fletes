package com.example.fletes.ui.camion

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.fletes.ui.camion.components.MyInsertCamionDialogContent

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
        title = { Text("Ingrese Camion") },
        text = {
            MyInsertCamionDialogContent(
                input1 = camionUiState.choferName,
                label1 = "Driver Name",
                onValueChange1 = { camionViewModel.onChoferNameValueChange(it) } ,
                input2 = camionUiState.choferDni.toString(),
                label2 = "Driver License",
                error2 = camionUiState.choferDniError.toString(),
                onValueChange2 = { camionViewModel.onChoferDniValueChange(it) },
                input3 = camionUiState.patenteTractor,
                label3 = "Truck License Plate",
                onValueChange3 = { camionViewModel.onPatenteTractorValueChange(it) },
                error3 = camionUiState.patenteError,
                input4 = camionUiState.patenteJaula,
                label4 = "Trailer License Plate",
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
