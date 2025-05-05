package com.example.fletes.ui.screenTruck.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fletes.R
import com.example.fletes.data.room.Camion
import java.time.LocalDate

@Composable
fun ListOfTrucks(
    listOfTrucks: List<Camion>,
    onDeleteTruck: (Int) -> Unit,
    onEditTruck: (Int) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(modifier = Modifier) {
            items(listOfTrucks) { truck ->
                TruckCard(
                    truck = truck,
                    onDeleteTruck = onDeleteTruck,
                    onEditTruck = onEditTruck,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun TruckCard(
    truck: Camion,
    onDeleteTruck: (Int) -> Unit,
    onEditTruck: (Int) -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .padding(2.dp)
            .fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TruckDetailsColumn(
                    truck = truck,
                    modifier = Modifier.weight(1f))
                InteractColumTruck(
                    truck = truck,
                    onDeleteTruck = onDeleteTruck,
                    onEditTruck = onEditTruck
                )
            }
        }
    }
}

@Composable
private fun InteractColumTruck(
    truck: Camion,
    onDeleteTruck: (Int) -> Unit,
    onEditTruck: (Int) -> Unit
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "ID: ${truck.id}", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.padding(vertical = 8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_delete_outline_24),
            contentDescription = "Delete",
            modifier = Modifier.clickable { onDeleteTruck(truck.id) }
        )
        Spacer(Modifier.padding(vertical = 8.dp))
        Icon(
            painter = painterResource(R.drawable.edit_edit_24),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "Edit",
            modifier = Modifier
                .clickable {
                    onEditTruck(truck.id)
                }
        )
    }
}
@Composable
fun DetailRow(
    label: String,
    value: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$label: $value",
            style = style
        )
    }
}



@Composable
fun TruckDetailsColumn(truck: Camion, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DetailRow(
            label = "Driver",
            value = truck.choferName,
            style = MaterialTheme.typography.titleMedium
        )
        DetailRow(label = "DNI", value = truck.choferDni.toString())
        DetailRow(label = "License Tractor", value = truck.patenteTractor)
        DetailRow(label = "License Trailer", value = truck.patenteJaula)
    }
}
@Preview(showBackground = true)
@Composable
fun InteractColumPreview() {
    val mockCamion = Camion(
        id = 1,
        createdAt = LocalDate.now(),
        choferName = "Jonh Doe",
        choferDni = 123456789,
        patenteTractor = "AA123BB",
        patenteJaula = "CC456DD",
        isActive = true,
    )
    InteractColumTruck(truck = mockCamion, onDeleteTruck = {}, onEditTruck = {})
}

@Preview(showBackground = true)
@Composable
fun DetailRowPreview() {
    DetailRow(
        label = "Chofer",
        value = "John Doe",
        style = MaterialTheme.typography.titleMedium
    )
}

@Preview(showBackground = true)
@Composable
fun TruckDetailsColumnPreview() {
    val mockCamion = Camion(
        id = 1,
        choferName = "John Doe",
        choferDni = 12345678,
        patenteTractor = "AB123CD",
        patenteJaula = "EF456GH",
        createdAt = LocalDate.now(),
        isActive = true
    )
    TruckDetailsColumn(truck = mockCamion)
}





@Preview(showBackground = true)
@Composable
fun TruckCardPreview() {
    val mockCamion = Camion(
        id = 1,
        choferName = "John Doe",
        choferDni = 12345678,
        patenteTractor = "AB123CD",
        patenteJaula = "EF456GH",
        createdAt = LocalDate.now(),
        isActive = true
    )
    TruckCard(
        truck = mockCamion,
        onDeleteTruck = {},
        onEditTruck = {},
        modifier = Modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ListOfTrucksPreview() {
    val camiones = listOf(
        Camion(
            id = 1,
            createdAt = LocalDate.now(),
            choferName = "Chofer 1",
            choferDni = 12345678,
            patenteTractor = "PATENTE1",
            patenteJaula = "JAULA1",
            isActive = true
        ),
        Camion(
            id = 2,
            createdAt = LocalDate.now(),
            choferName = "Chofer 1",
            choferDni = 12345678,
            patenteTractor = "PATENTE1",
            patenteJaula = "JAULA1",
            isActive = true
        ),
        Camion(
            id = 13,
            createdAt = LocalDate.now(),
            choferName = "Chofer 1",
            choferDni = 12345678,
            patenteTractor = "PATENTE1",
            patenteJaula = "JAULA1",
            isActive = true
        ),
    )

    Column {
        ListOfTrucks(
            listOfTrucks = camiones,
            onDeleteTruck = {},
            onEditTruck = {},
            modifier = Modifier
        )
    }
}


