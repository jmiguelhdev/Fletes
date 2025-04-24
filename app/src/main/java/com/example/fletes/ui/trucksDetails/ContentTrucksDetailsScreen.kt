package com.example.fletes.ui.trucksDetails

import android.icu.text.NumberFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import com.example.fletes.R
import com.example.fletes.data.room.Destino
import com.example.fletes.ui.dispatch.DeleteDestinoAlertDialog
import com.example.fletes.ui.theme.FletesTheme
import java.time.LocalDate
import java.util.Locale

@Composable
fun ContentTrucksDetailsScreen(
    modifier: Modifier = Modifier,
    activeDispatch: List<Destino> = emptyList(),
    showDeleteDialog: Boolean = false,
    onDismissRequest: () ->Unit,
    onConfirm: (Destino) -> Unit = {},
    onDeleteClick: () -> Unit,
    onEditClick: (Destino) -> Unit

) {
    ActiveDispatch(
        activeDispatch = activeDispatch,
        onDeleteClick = onDeleteClick,
        showDeleteDialog = showDeleteDialog,
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        onEditClick = onEditClick,
        modifier = modifier
    )
}

@Composable
fun AvailableTrucks(modifier: Modifier = Modifier) {

}

@Composable
fun ActiveDispatch(
    modifier: Modifier = Modifier,
    activeDispatch: List<Destino>,
    showDeleteDialog: Boolean = false,
    onDismissRequest :()-> Unit,
    onConfirm: (Destino) -> Unit = {},
    onDeleteClick: () -> Unit,
    onEditClick: (Destino) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(
            items = activeDispatch,
            key = { destino -> destino.id }
            ) { destino ->
            DispatchCard(
                dispatch = destino,
                onDeleteClick = onDeleteClick,
                onEditClick = onEditClick,
            )
            if (showDeleteDialog) {
                DeleteDestinoAlertDialog(
                    destino = destino,
                    onDismissRequest = onDismissRequest,
                    onConfirm = onConfirm,
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
    onEditClick: (dispatch: Destino) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

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
                                .clickable { onEditClick(dispatch) }
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
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DispatchCardPrev(modifier: Modifier = Modifier) {
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
            onEditClick = {}
        )
    }
}