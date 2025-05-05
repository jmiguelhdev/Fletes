package com.example.fletes.ui.screenActiveDispatch.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun TruckFab(
    modifier: Modifier = Modifier,
    icon: Painter,
    onClick: () -> Unit = {}
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            painter = icon,
            contentDescription = "Truck Icon"
        )
    }
}