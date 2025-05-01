package com.example.fletes.ui.screenTrucksJourney

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun truckRegistrationScreen() {

}


@Composable
fun SaveOrUpdateTripButton(
    modifier: Modifier = Modifier,
    destinationId: Int,
    isActive: Boolean, //implementar
    onClickSaveOrUpdateTrip: (destinoId: Int) -> Unit = {},
) {
    OutlinedButton(
        onClick = { onClickSaveOrUpdateTrip(destinationId) },
        modifier = modifier.fillMaxWidth(),
        enabled = isActive
    ) {
        Text(text = "Save or Update Trip")
    }

}



