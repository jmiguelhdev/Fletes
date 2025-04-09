package com.example.fletes.ui.camion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MyInsertCamionDialogContent(
    input1: String,
    label1: String = "Input Driver Name",
    onValueChange1: (String) -> Unit,
    input2: String,
    label2: String = "Input Driver License",
    error2msg: String? = null,
    isValid2: Boolean = true,
    onValueChange2: (String) -> Unit,
    input3: String,
    label3: String = "Input Truck License Plate",
    error3msg: String? = null,
    isValid3: Boolean = true,
    onValueChange3: (String) -> Unit,
    input4: String,
    label4: String = "Input Trailer License Plate",
    error4msg: String? = null,
    isValid4: Boolean = true,
    onValueChange4: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        MyTextField(
            input = input1,
            label = label1,
            onValueChange = onValueChange1
        )
        Spacer(modifier = Modifier.height(4.dp))
        MyTextField(
            input = input2,
            label = error2msg ?: label2,
            onValueChange = onValueChange2,
            isValid = isValid2
        )
        Spacer(modifier = Modifier.height(4.dp))
        MyTextField(
            input = input3,
            label = error3msg ?: label3,
            onValueChange = onValueChange3,
            isValid = isValid3
        )
        Spacer(modifier = Modifier.height(4.dp))
        MyTextField(
            input = input4,
            label = error4msg ?: label4,
            onValueChange = onValueChange4,
            isValid = isValid4
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyDialogContentPreview() {
    val state1 = remember { mutableStateOf("") }
    val state2 = remember { mutableStateOf("") }
    val state3 = remember { mutableStateOf("") }
    val state4 = remember { mutableStateOf("") }
    MyInsertCamionDialogContent(
        input1 = state1.value,
        onValueChange1 = { state1.value = it },
        input2 = state2.value,
        onValueChange2 = { state2.value = it },
        input3 = state3.value,
        onValueChange3 = { state3.value = it },
        input4 = state4.value,
        onValueChange4 = { state4.value = it }
    )
}