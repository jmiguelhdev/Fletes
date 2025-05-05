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
import com.example.fletes.data.room.Camion
import com.example.fletes.ui.theme.FletesTheme
import java.time.LocalDate

val listaDeCamiones = listOf(
    Camion(
        id = 1,
        createdAt = LocalDate.now(),
        choferName = "John Doe",
        choferDni = 231231,
        patenteTractor = "122313",
        patenteJaula = "2123132",
        isActive = true
    ),
    Camion(
        id = 1,
        createdAt = LocalDate.now(),
        choferName = "John Doe",
        choferDni = 231231,
        patenteTractor = "122313",
        patenteJaula = "2123132",
        isActive = true
    ),
    Camion(
        id = 1,
        createdAt = LocalDate.now(),
        choferName = "John Doe",
        choferDni = 231231,
        patenteTractor = "122313",
        patenteJaula = "2123132",
        isActive = true
    ),
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrucksDropdown(
    list: List<Camion>,
    onClickTruck: (Camion) -> Unit
) {
    val listOfChoferName = list.filter { it.choferName.isNotBlank() }.map { it.choferName }


    val options: List<String> = listOfChoferName
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
            label = { Text("Select a Truck") },
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
                        onClickTruck(list.find { it.choferName == option }!!)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrucksDropdownPreview() {
    FletesTheme {
        Column() {
            TrucksDropdown(
                onClickTruck = {},
                list = listaDeCamiones,
            )
        }
    }
}
