package com.example.fletes.ui.screenTrucksJourney.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.example.fletes.data.room.Camion
import java.time.LocalDate

@Composable
fun TruckChipRow(
    camionList: List<Camion>,
    onClick: (camion: Camion) -> Unit = {}
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            camionList
        ) {
            TruckChip(
                camion = it,
                isChipActive = it.isActive,
                onClick = onClick
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TruckChipRowPrev() {
    val camionList = listOf(
        Camion(
            id = 1,
            choferName = "Juan Perez",
            createdAt = LocalDate.now(),
            choferDni = 29384756,
            patenteTractor = "ad123fg",
            patenteJaula = "df213fg",
            isActive = true,
        ),
        Camion(
            id = 2,
            choferName = "Roberto Carlos",
            createdAt = LocalDate.now(),
            choferDni = 29384756,
            patenteTractor = "ad123fg",
            patenteJaula = "df213fg",
            isActive = true,
        )
    )
    TruckChipRow(camionList = camionList)
}

@Composable
fun TruckChip(
    camion: Camion,
    isChipActive: Boolean = true,
    onClick: (camion: Camion) -> Unit = {}
) {
    ElevatedAssistChip(
        onClick = {
            onClick(camion)
        },
        label = { Text("Chofer: ${camion.choferName}") },
        modifier = Modifier
            .padding(4.dp)
            .wrapContentWidth(),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (isChipActive) Color.Green else Color.Red,
            labelColor = Color.White,
            leadingIconContentColor = Color.White, // Color para el icono si lo hubiera
            trailingIconContentColor = Color.White,
        )
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    wallpaper = Wallpapers.NONE
)
@Composable
fun PreviewTruckChip() {
    val camion = Camion(
        id = 1,
        choferName = "Juan Perez",
        createdAt = LocalDate.now(),
        choferDni = 29384756,
        patenteTractor = "ad123fg",
        patenteJaula = "df213fg",
        isActive = true,
    )
    TruckChip(camion = camion)
}