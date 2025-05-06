package com.example.fletes.ui.screenActiveDispatch.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fletes.data.room.Destino
import com.example.fletes.ui.theme.FletesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationDropdown(
    list: List<Destino>,
    onClickDestination: (Destino) -> Unit
) {
    val listOfDestinos: List<String> = list.filter { it.comisionista.isNotBlank() }.map { it.comisionista }
    val options: List<String> = if (listOfDestinos.isEmpty()) listOf("") else listOfDestinos
    var expanded by remember { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState(options[0])

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.padding(8.dp)
    ) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field to handle
            // expanding/collapsing the menu on click. A read-only text field has
            // the anchor type `PrimaryNotEditable`.
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            state = textFieldState,
            readOnly = true,
            lineLimits = TextFieldLineLimits.SingleLine,
            label = { Text("Select a Destination") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        textFieldState.setTextAndPlaceCursorAtEnd(option)
                        expanded = false
                        onClickDestination(list.find { it.comisionista == option }!!)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DestinationDropdownPreview() {
    FletesTheme {
        Column() {
            TrucksDropdown(
                onClickTruck = {},
                list = listaDeCamiones,
            )
        }
    }
}