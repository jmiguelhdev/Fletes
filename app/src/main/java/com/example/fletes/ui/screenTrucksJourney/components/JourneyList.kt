package com.example.fletes.ui.screenTrucksJourney.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.CamionesRegistro
import com.example.fletes.data.room.Destino
import com.example.fletes.data.room.JourneyWithAllDetails
import java.time.LocalDate

@Composable
fun JourneyList(
    journeyList: List<JourneyWithAllDetails>,
    modifier: Modifier = Modifier,
    onClickCard: (journey: CamionesRegistro) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(journeyList) { journeyWithDetails ->
            SingleDestinationCard(
                camionesRegistro = journeyWithDetails,
                onClickCard = onClickCard
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JourneyListPreview() {
    val mockJourneyRegistro1 = CamionesRegistro(
        id = 1,
        camionId = 1,
        destinoId = 1,
        createdAt = LocalDate.now().minusDays(1),
        kmCarga = 1000,
        kmDescarga = 2000,
        kmSurtidor = 500,
        litros = 100.0,
        isActive = true
    )
    val mockJourneyRegistro2 = CamionesRegistro(
        id = 2,
        camionId = 2,
        destinoId = 2,
        createdAt = LocalDate.now(),
        kmCarga = 2000,
        kmDescarga = 3000,
        kmSurtidor = 600,
        litros = 150.0,
        isActive = true
    )
    val mockCamion1 = Camion(
        id = 1,
        createdAt = LocalDate.now(),
        choferName = "John Doe",
        choferDni = 12345678,
        patenteTractor = "ABC123",
        patenteJaula = "DEF456",
        isActive = true
    )
    val mockCamion2 = Camion(
        id = 2,
        createdAt = LocalDate.now(),
        choferName = "Jane Smith",
        choferDni = 87654321,
        patenteTractor = "GHI789",
        patenteJaula = "JKL012",
        isActive = true
    )
    val mockDestino1 = Destino(
        id = 1,
        createdAt = LocalDate.now(),
        comisionista = "Miguel",
        despacho = 1012.0,
        localidad = "Tucuman",
        isActive = true
    )
    val mockDestino2 = Destino(
        id = 2,
        createdAt = LocalDate.now(),
        comisionista = "Carlos",
        despacho = 2024.0,
        localidad = "Salta",
        isActive = true
    )

    val mockJourneyWithAllDetails1 = JourneyWithAllDetails(
        journey = mockJourneyRegistro1,
        camion = mockCamion1,
        destino = mockDestino1
    )
    val mockJourneyWithAllDetails2 = JourneyWithAllDetails(
        journey = mockJourneyRegistro2,
        camion = mockCamion2,
        destino = mockDestino2
    )

    val journeyList = listOf(mockJourneyWithAllDetails1, mockJourneyWithAllDetails2)

    JourneyList(journeyList = journeyList, onClickCard = {})
}
