package com.example.fletes.ui.screenTrucksJourney.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.CamionesRegistro
import com.example.fletes.data.room.Destino
import com.example.fletes.data.room.JourneyWithAllDetails
import java.time.LocalDate

@Composable
fun SingleDestinationCard(
    modifier: Modifier = Modifier,
    camionesRegistro: JourneyWithAllDetails,
    onClickCard: (journey: CamionesRegistro) -> Unit
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                onClickCard(camionesRegistro.journey)
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
                    text = "Created At: ${camionesRegistro.journey.createdAt}",
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Destino: ${camionesRegistro.destino.localidad}",
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Chofer: ${camionesRegistro.camion.choferName}",
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingleDestinationCardPreview() {
    val mockJourneyRegistro = CamionesRegistro(
        id = 1,
        camionId = 1,
        destinoId = 1,
        createdAt = LocalDate.now(),
        kmCarga = 1000,
        kmDescarga = 2000,
        kmSurtidor = 500,
        litros = 100.0,
        isActive = true
    )
    val mockCamion = Camion(
        id = 1,
        createdAt = LocalDate.now(),
        choferName = "John Doe",
        choferDni = 12345678,
        patenteTractor = "ABC123",
        patenteJaula = "DEF456",
        isActive = true
    )
    val mockDestino = Destino(
        id = 1,
        createdAt = LocalDate.now(),
        comisionista = "Miguel",
        despacho = 1012.0,
        localidad = "Tucuman",
        isActive = true
    )
    val mockJourneyWithAllDetails = JourneyWithAllDetails(
        journey = mockJourneyRegistro,
        camion = mockCamion,
        destino = mockDestino
    )

    SingleDestinationCard(
        camionesRegistro = mockJourneyWithAllDetails,
        onClickCard = {}
    )
}
