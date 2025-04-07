package com.example.fletes.domain


data class DniValidationResult(
    val isValid: Boolean,
    val dni: Int? = null,
    val error: String? = null
)

class DniValidator {

    fun validateDni(newValue: String): DniValidationResult {
        if (newValue.isEmpty()) {
            return DniValidationResult(isValid = true, dni = null, error = "Insert DNI")
        }
        if (!newValue.all { it.isDigit() }) {
            return DniValidationResult(isValid = false, dni = null, error = "DNI must contain only digits")
        }

        val intValue = newValue.toIntOrNull()
        val isValidDni = intValue?.let { it in MIN_DNI_VALUE..MAX_DNI_VALUE } ?: false

        return if (isValidDni) {
            DniValidationResult(isValid = true, dni = intValue, error = "Dni correct")
        } else {
            DniValidationResult(isValid = false, dni = null, error = "Invalid DNI number")
        }
    }

    companion object {
        private const val MIN_DNI_VALUE = 20_000_000
        private const val MAX_DNI_VALUE = 99_000_000
    }
}