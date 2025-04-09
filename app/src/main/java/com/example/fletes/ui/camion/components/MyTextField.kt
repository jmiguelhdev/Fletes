package com.example.fletes.ui.camion.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    onValueChange: (String) -> Unit,
    isValid: Boolean = true
) {
    OutlinedTextField(
        modifier = modifier,
        value = input,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = true,
        isError = !isValid,
        maxLines = 1,
        colors = OutlinedTextFieldDefaults.colors(
            errorBorderColor = if (isValid) {MaterialTheme.colorScheme.primary} else {MaterialTheme.colorScheme.error},
        )
    )
}

@Preview(showBackground = true)
@Composable
fun MyTextFieldPreview(){
    val state = remember { mutableStateOf("") }
    val isError by remember { mutableStateOf(false) }
    MyTextField(
        input = state.value,
        label = "Preview",
        onValueChange = {
            state.value = it
        },
        isValid = isError,

    )


}

