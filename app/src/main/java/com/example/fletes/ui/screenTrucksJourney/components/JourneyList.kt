package com.example.fletes.ui.screenTrucksJourney.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.CamionesRegistro

@Composable
fun JourneyList(
    camion: Camion,
    journeyList: List<CamionesRegistro>,
    modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(journeyList) {

        }
    }
}