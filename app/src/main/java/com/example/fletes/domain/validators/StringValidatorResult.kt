package com.example.fletes.domain.validators

data class StringValidatorResult(
    val isValid: Boolean = false,
    val value: String? = null,
    val errorMessage: String? = null
)