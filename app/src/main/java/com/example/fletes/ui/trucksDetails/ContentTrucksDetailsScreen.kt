package com.example.fletes.ui.trucksDetails

import android.icu.text.NumberFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fletes.R
import com.example.fletes.data.room.Destino
import com.example.fletes.ui.theme.FletesTheme
import java.time.LocalDate
import java.util.Locale

@Composable
fun ContentTrucksDetailsScreen(modifier: Modifier = Modifier) {
    Column {

    }
}

@Composable
fun AvailableTrucks(modifier: Modifier = Modifier) {

}

@Composable
fun ActiveDispatch(
    modifier: Modifier = Modifier,
    activeDispatch: List<Destino> = emptyList()
) {

}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DispatchCard(
    modifier: Modifier = Modifier,
    dispatch: Destino,
    onClick: () -> Unit,
    onDeleteClick: (dispatch: Destino) -> Unit,
    onEditClick: (dispatch: Destino) -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.padding(8.dp) // Optional padding around the card
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Padding inside the card
            horizontalAlignment = Alignment.CenterHorizontally // Center items horizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // Icons at the end
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dispatch.createdAt.toString(),
                    fontSize = 18.sp
                )

                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete_24),
                        contentDescription = "delete icon",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { onDeleteClick(dispatch) }
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
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), // Vertical padding for spacing
                color = Color.Black,
                thickness = 2.dp
            )
            Text(text = "Commission agent Name: ${dispatch.comisionista}")
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Divider()

            val format: NumberFormat = NumberFormat
                .getCurrencyInstance(Locale.getDefault())
            val formattedNumber = format.format(dispatch.despacho)
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "Dispatch amount: $formattedNumber")
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp), // Padding for spacing
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
            Divider()
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
            onClick = {},
            onDeleteClick = {},
            onEditClick = {}
        )
    }
}