package com.example.fletes.ui.screenActiveDispatch.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fletes.R
import com.example.fletes.data.room.Destino

@Composable
fun InteractColumDestination(
    destination: Destino,
    onDeleteDestination: (destination: Destino) -> Unit,
    onEditDestination: (destination: Destino) -> Unit
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "ID: ${destination.id}", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.padding(vertical = 8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_delete_outline_24),
            contentDescription = "Delete",
            modifier = Modifier.clickable {
                onDeleteDestination(destination)
            }
        )
        Spacer(Modifier.padding(vertical = 8.dp))
        Icon(
            painter = painterResource(R.drawable.edit_edit_24),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "Edit",
            modifier = Modifier
                .clickable {
                    onEditDestination(destination)
                }
        )
    }
}