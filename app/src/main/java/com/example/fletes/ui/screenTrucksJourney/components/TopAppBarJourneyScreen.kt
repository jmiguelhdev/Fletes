package com.example.fletes.ui.screenTrucksJourney.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fletes.ui.theme.FletesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            if (checked) Text("Active Journeys") else Text("All Journeys")
        },
        actions = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun TopAppBarSwitchPreview() {
    FletesTheme {
        TopAppBarSwitch(checked = true, onCheckedChange = {})
    }
}
