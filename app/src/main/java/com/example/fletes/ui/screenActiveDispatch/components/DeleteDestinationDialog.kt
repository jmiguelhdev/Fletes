package com.example.fletes.ui.screenActiveDispatch.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fletes.data.room.Destino

@Composable
fun DeleteDestinationDialog(
    destino: Destino,
    onDismissRequestDelete: () -> Unit,
    onConfirmDelete: (destino: Destino) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequestDelete,
        confirmButton = {
            Button(onClick = { onConfirmDelete(destino) }) {
                Text(text = "Confirm Delete")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequestDelete) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = "Delete Destination ${destino.localidad}")
        },
        text = {
            Column {
                Text(text = "Are you sure you want to delete this destination?")
                Text(text = "This action cannot be undone.")
                Text(text = "Id: ${destino.id}")
                Text(text = "Fecha: ${destino.createdAt}")
                Text(text = "Despacho: ${destino.despacho}")
                Text(text = "Comisionista: ${destino.comisionista}")
                Text(text = "Destino: ${destino.localidad}")
            }
        },
        modifier = modifier
    )

}