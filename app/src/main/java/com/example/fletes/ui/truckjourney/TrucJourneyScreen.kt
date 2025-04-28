package com.example.fletes.ui.truckjourney

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
import com.example.fletes.data.room.Camion
import com.example.fletes.ui.dispatch.DecimalTextField
import com.example.fletes.ui.theme.FletesTheme
import java.time.LocalDate

@Composable
fun truckRegistrationScreen() {

}

@Composable
fun JourneyCard(
    modifier: Modifier = Modifier,
    camion: Camion,
    valueKmCarga: Double,
    onValueChangeKmCarga: (String) -> Unit,
    errorMessage: String,
) {
    Card(modifier = Modifier.padding(8.dp))  {
        Row (
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
                        value = valueKmCarga.toString(),
                        onValueChange = onValueChangeKmCarga,
                        label = "km carga",
                        errorMessage = errorMessage,
                    )
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(4.dp)) {
                    DecimalTextField(
                        value = valueKmCarga.toString(),
                        onValueChange = onValueChangeKmCarga,
                        label = "km descarga",
                        errorMessage = errorMessage,                    )
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(4.dp)) {
                    DecimalTextField(
                        value = valueKmCarga.toString(),
                        onValueChange = onValueChangeKmCarga,
                        label = "km en surtidor",
                        errorMessage = errorMessage,                    )
                }
            }
        }
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
        kmService = 20000
    )

    FletesTheme {
        JourneyCard(
            camion = sampleCamion,
            valueKmCarga = 123234.45,
            onValueChangeKmCarga = { /* Handle value change */ },
            errorMessage = "Error"
        )
    }
}
