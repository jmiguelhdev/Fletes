package com.example.fletes.data.model

data class DecimalTextFieldData(
    val label: String,
    val value: String,
    val onValueChange: (String) -> Unit,
    val errorMessage: String,
)