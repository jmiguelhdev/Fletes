package com.example.fletes.ui.screenTrucksJourney.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fletes.R
import com.example.fletes.data.room.Destino
import java.time.LocalDate

@Composable
fun SingleDestinationCard(
    modifier: Modifier = Modifier,
    destination: Destino,
    onClicable: (destination: Destino) -> Unit
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                Log.d("DestinationCard", "Destination clicked: $destination")
                destination.isActive
                onClicable(destination)
            },

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Created At: ${destination.createdAt}",
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                HorizontalDivider(modifier = Modifier.padding(4.dp))
                Text(
                    text = "Commission Agent: ${destination.comisionista}",
                    modifier = Modifier.padding(4.dp)
                )
                HorizontalDivider(modifier = Modifier.padding(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Location: ${destination.localidad}")
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
                        .padding(top = 4.dp),
                    textAlign = TextAlign.Center
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingleDestinationCardPreview() {
    SingleDestinationCard(
        destination = Destino(
            id = 1,
            createdAt = LocalDate.now(),
            comisionista = "John Doe",
            despacho = 12345.0,
            localidad = "Silent Hill",
            isActive = true
        ),
        onClicable = {}
    )
}