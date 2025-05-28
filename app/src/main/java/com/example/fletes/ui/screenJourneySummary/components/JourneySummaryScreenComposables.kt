package com.example.fletes.ui.screenJourneySummary.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fletes.data.room.JourneyWithBuyDetails
import java.util.Locale // Required for String.format with Locale

@Composable
fun JourneySummaryListItem(details: JourneyWithBuyDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Added padding for spacing between cards in a list
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Driver: ${details.camion.choferName}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Localidad: ${details.destino.localidad}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Comisionista: ${details.destino.comisionista}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Despacho: ${details.destino.despacho}", // Assuming despacho is a number, converting to String
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Distance: ${details.calculatedDistance} km",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            val buyDetails = details.buy
            Text(
                text = if (buyDetails?.kg != null) "Kg: ${String.format(Locale.US, "%.2f", buyDetails.kg)}" else "Kg: N/A",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = if (buyDetails?.price != null) "Price: ${String.format(Locale.US, "%.2f", buyDetails.price)}" else "Price: N/A",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = if (buyDetails?.kgFaena != null) "Kg Faena: ${String.format(Locale.US, "%.2f", buyDetails.kgFaena)}" else "Kg Faena: N/A",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
