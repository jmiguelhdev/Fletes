package com.example.fletes.ui.screenActiveDispatch.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.fletes.data.room.Camion

@Composable
fun CardSelectedTruck(
    truck: Camion,
    unSelectTruck: (Camion) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                unSelectTruck(truck)
            },
        colors = CardDefaults.cardColors(
            containerColor = if (truck.isActive) Color.Green else Color.Red,
        )
    ) {
        HorizontalDivider()
        Text(
            text = truck.choferName,
            modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        HorizontalDivider()
    }
}


val truck = Camion(
    id = 1,
    choferName = "Juan Perez",
    choferDni = 12345678,
    patenteTractor = "ABC123",
    patenteJaula = "DEF456",
    isActive = false
)

@Preview(showBackground = true)
@Composable
fun PreviewCardSelectedTruck() {
    CardSelectedTruck(truck = truck, unSelectTruck = {
    })
}