package com.example.fletes.ui.screenActiveDispatch.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fletes.data.room.Destino
import com.example.fletes.ui.screenDispatch.DecimalTextField
import com.example.fletes.ui.theme.FletesTheme
import java.time.LocalDate


@Composable
fun UpdateDestinationAlertDialog(
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),

                    value = value,

                    onValueChange = onValueChange,
                    label = "Despacho",
                    errorMessage = errorMessage,
                )
            }
        }, modifier = Modifier.wrapContentHeight()
    )
}

@Preview(showBackground = true)
@Composable
fun UpdateDestinoAlertDialogPreview(modifier: Modifier = Modifier) {
    FletesTheme {
        UpdateDestinationAlertDialog(
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

