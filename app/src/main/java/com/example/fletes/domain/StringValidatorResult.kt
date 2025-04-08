package com.example.fletes.domain

data class StringValidatorResult(
    val isValid: Boolean = false,
    val value: String? = null,
    val error: String? = null
)
