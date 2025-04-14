package com.example.fletes.domain.validators

import com.example.fletes.domain.validators.StringValidatorResult

/**
 * DniValidator is a utility class responsible for validating DNI (Documento Nacional de Identidad) strings.
 * It checks if the provided string is a valid DNI based on the following criteria:
 *   - It must not be empty.
 *   - It must contain only digits.
 *   - It must be within the valid DNI range (20,000,000 to 99,000,000).
 */
class DniValidator {

    fun validateDni(newValue: String): StringValidatorResult {
        if (newValue.isEmpty()) {
            return StringValidatorResult(isValid = true, value = null, errorMessage = "Insert DNI")
        }
        if (!newValue.all { it.isDigit() }) {
            return StringValidatorResult(
                isValid = false,
                value = null,
                errorMessage = "DNI must contain only digits"
            )
        }

        val intValue = newValue.toIntOrNull()
        val isValidDni = intValue?.let { it in MIN_DNI_VALUE..MAX_DNI_VALUE } ?: false

        return if (isValidDni) {
            StringValidatorResult(
                isValid = true,
                value = intValue.toString(),
                errorMessage = "Dni correct"
            )
        } else {
            StringValidatorResult(
                isValid = false,
                value = null,
                errorMessage = "Invalid DNI number"
            )
        }
    }

    companion object {
        private const val MIN_DNI_VALUE = 20_000_000
        private const val MAX_DNI_VALUE = 99_000_000
    }
}