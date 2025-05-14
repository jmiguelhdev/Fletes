package com.example.fletes.ui.screenTrucksJourney.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fletes.data.room.CamionesRegistro

@Composable
fun JourneyCardCollapsed(
    journey: CamionesRegistro,
    modifier: Modifier = Modifier
) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "Registro de viaje Chofer: ")
        }

    }
}