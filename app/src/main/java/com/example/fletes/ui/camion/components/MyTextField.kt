package com.example.fletes.ui.camion.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    input: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = input,
        onValueChange = onValueChange,
        label = { Text(text = label) }
    )
}

@Preview(showBackground = true)
@Composable
fun MyTextFieldPreview(){
    val state = remember { mutableStateOf("") }
    MyTextField(input = state.value, label = "Preview", onValueChange = { state.value = it })

}

