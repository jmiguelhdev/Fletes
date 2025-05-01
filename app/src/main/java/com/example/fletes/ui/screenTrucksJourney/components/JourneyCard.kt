package com.example.fletes.ui.screenTrucksJourney.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fletes.data.model.DecimalTextFieldData
import com.example.fletes.data.model.truckJourneyData.TruckJourneyData
import com.example.fletes.data.room.Camion
import com.example.fletes.ui.screenDispatch.DecimalTextField
import com.example.fletes.ui.theme.FletesTheme
import java.time.LocalDate

@Composable
fun JourneyCard(
    modifier: Modifier = Modifier,
    camion: Camion,
    truckJourneyData: TruckJourneyData,
) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Registro de viaje Chofer: ")
            Text(text = camion.choferName)
        }
        HorizontalDivider(thickness = DividerDefaults.Thickness)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(4.dp)) {
                    DecimalTextField(
                        value = truckJourneyData.kmCargaData.value,
                        onValueChange = truckJourneyData.kmCargaData.onValueChange,
                        label = "km carga",
                        errorMessage = truckJourneyData.kmCargaData.errorMessage,
                    )
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(4.dp)) {
                    DecimalTextField(
                        value = truckJourneyData.kmDescargaData.value,
                        onValueChange = truckJourneyData.kmDescargaData.onValueChange,
                        label = "km descarga",
                        errorMessage = truckJourneyData.kmDescargaData.errorMessage,
                    )
                }
            }
        }
        HorizontalDivider(thickness = DividerDefaults.Thickness)
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(4.dp)) {
                    DecimalTextField(
                        value = truckJourneyData.kmSurtidorData.value,
                        onValueChange = truckJourneyData.kmSurtidorData.onValueChange,
                        label = "km surtidor",
                        errorMessage = truckJourneyData.kmDescargaData.errorMessage,
                    )
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(4.dp)) {
                    DecimalTextField(
                        value = truckJourneyData.litrosData.value,
                        onValueChange = truckJourneyData.litrosData.onValueChange,
                        label = "litros surtidos",
                        errorMessage = truckJourneyData.litrosData.errorMessage,
                    )
                }
            }
        }
        HorizontalDivider(thickness = DividerDefaults.Thickness)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun JourneyCardPreview() {
    val sampleCamion = Camion(
        id = 1,
        createdAt = LocalDate.now(),
        choferName = "Miguel",
        choferDni = 29673971,
        patenteTractor = "Ad123dc",
        patenteJaula = "Cd456Fd",
        isActive = true
    )
    // Creamos un objeto de ejemplo de TruckJourneyData
    // Cada campo (kmCargaData, kmDescargaData, etc.) ahora es un TextFieldData
    val sampleTruckJourneyData = TruckJourneyData(
        camionId = 0,
        kmCargaData = DecimalTextFieldData(
            label = "km carga",
            value = "123.0",
            onValueChange = { /* Aquí puedes agregar lógica de manejo de cambios si es necesario */ },
            errorMessage = ""
        ),
        kmDescargaData = DecimalTextFieldData(
            label = "km descarga",
            value = "231.0",
            onValueChange = { /* Aquí puedes agregar lógica de manejo de cambios si es necesario */ },
            errorMessage = ""
        ),
        kmSurtidorData = DecimalTextFieldData(
            label = "km surtidor",
            value = "100.0",
            onValueChange = { /* Aquí puedes agregar lógica de manejo de cambios si es necesario */ },
            errorMessage = ""
        ),
        litrosData = DecimalTextFieldData(
            label = "litros surtidos",
            value = "50.0",
            onValueChange = { /* Aquí puedes agregar lógica de manejo de cambios si es necesario */ },
            errorMessage = ""
        ),
        isActive = false
    )

    FletesTheme {
        JourneyCard(
            camion = sampleCamion,
            truckJourneyData = sampleTruckJourneyData,
        )
    }
}
