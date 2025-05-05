package com.example.fletes.ui.screenActiveDispatch.components

import android.content.res.Configuration
import androidx.compose.animation.core.copy
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fletes.R
import com.example.fletes.data.model.destinations.Destination

@Composable
fun DestinationCard(
    modifier: Modifier = Modifier,
    destination: Destination,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(true) }
    val cardColor = if (isChecked) Green.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f)
                    .clickable { isChecked = !isChecked }
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                (destination.comisionista ?: destination.localidad)?.let {
                    Text(text = "Commission Agent: $it", modifier = Modifier.padding(4.dp))
                }
                HorizontalDivider(modifier = Modifier.padding(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    destination.localidad?.let {
                        Text(text = "Location: $it")
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.ic_location_pin_24),
                        contentDescription = "Location",
                        modifier = Modifier.padding(4.dp)
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(4.dp))
                Text(
                    text = "$ ${destination.despacho}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    textAlign = TextAlign.Center
                )

            }
            Spacer(modifier = Modifier)
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.Top),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vert_24),
                    contentDescription = "More options",
                    modifier = Modifier
                        .clickable { expanded = true }
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text("Editar") },
                    onClick = {
                        onEdit()
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Borrar") },
                    onClick = {
                        onDelete()
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    wallpaper = Wallpapers.NONE
)
@Composable
fun PreviewDestinationCard() {
    val destination = Destination(
        id = 1,
        comisionista = "Comisionista",
        localidad = "Localidad",
        despacho = 123
    )
    DestinationCard(destination = destination, onEdit = {}, onDelete = {})
}