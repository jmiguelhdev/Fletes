package com.example.fletes.ui.screenTrucksJourney.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.fletes.R
import com.example.fletes.data.model.DecimalTextFieldData
import com.example.fletes.data.model.truckJourneyData.TruckJourneyData
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.CamionesRegistro
import com.example.fletes.data.room.Destino
import java.time.LocalDate

@Composable
fun JourneyCardAnimated(
    expanded: Boolean,
    destino: Destino,
    camion: Camion,
    camionesRegistro: CamionesRegistro,
    truckJourneyData: TruckJourneyData,
    onClickCard:(journey: CamionesRegistro) -> Unit,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    ) {
        SingleDestinationCard(
            modifier = modifier,
            journey = camionesRegistro,
            camion = camion,
            destino = destino,
            onClickCard = onClickCard
        )
        Row {
            IconButton(
                onClick = onExpandClick,
                modifier = modifier.fillMaxWidth(),
            ) {
                if (expanded) {
                    Icon(
                        painter = painterResource(R.drawable.ic_expand_less_24),
                        contentDescription = null
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_expand_more_24),
                        contentDescription = null
                    )
                }
            }
        }
        if(expanded) {
            JourneyCardItems(
                camion = camion,
                destino = destino,
                truckJourneyData = truckJourneyData,
                onIsActiveChange = {}
            )
        }
    }
}

val mockTruckJourneyData = TruckJourneyData(
    camionId = 1,
    kmCargaData = DecimalTextFieldData(
        label = "Km Carga",
        value = "12500.5",
        onValueChange = { /* No-op for preview */ },
        errorMessage = ""
    ),
    kmDescargaData = DecimalTextFieldData(
        label = "Km Descarga",
        value = "12850.0",
        onValueChange = { /* No-op for preview */ },
        errorMessage = ""
    ),
    kmSurtidorData = DecimalTextFieldData(
        label = "Km Surtidor",
        value = "12900.2",
        onValueChange = { /* No-op for preview */ },
        errorMessage = "Revisar valor" // Example of an error message
    ),
    litrosData = DecimalTextFieldData(
        label = "Litros Surtidos",
        value = "350.75",
        onValueChange = { /* No-op for preview */ },
        errorMessage = ""
    ),
    isActive = true
)
val mockCamion = Camion(
    id = 1,
    createdAt = LocalDate.now(),
    choferName = "Juan",
    choferDni = 12345678,
    patenteTractor = "ABC123",
    patenteJaula = "DEF456",
    isActive = true
)

val mockDestino = Destino(
    id = 1,
    createdAt = LocalDate.now(),
    comisionista = "Juan",
    despacho = 12354.0,
    localidad = "Tucman",
    isActive = true
)

val mockCamionesRegistro = CamionesRegistro(
    id = 1,
    camionId = 1,
    destinoId = 1,
    createdAt = LocalDate.now(),
    kmCarga = 1000,
    kmDescarga = 2000,
    kmSurtidor = 500,
    litros = 200.0,
    isActive = true
)
//@Preview(showBackground = true)
//@Composable
//fun JourneyCardAnimatedPreview() {
//    JourneyCardAnimated(
//        expanded = true,
//        destino = mockDestino,
//        camion = mockCamion,
//        camionesRegistro = mockCamionesRegistro,
//        truckJourneyData = mockTruckJourneyData,
//        onExpandClick = {},
//        onClickCard = {},
//    )
//}