package com.example.fletes.domain.validators

import com.example.fletes.domain.validators.StringValidatorResult

class PatenteValidator {
    /**
     * Validates a license plate (patente) string according to two possible formats:
     *   - **Format 1:** Two uppercase letters, followed by three digits, followed by two uppercase letters (e.g., "AA123BB").
     *   - **Format 2:** Three uppercase letters, followed by three digits (e.g., "AAA123").
     *
     * The function converts the input to uppercase, checks if it matches either format using a regular expression,
     * and returns a `PatenteValidatorResult` indicating the validity and the formatted license plate (if valid) or an error message.
     *
     * @param newValue The license plate string to validate.
     * @return A `PatenteValidatorResult` object containing:
     *   - `isValid`: A boolean indicating whether the license plate is valid.
     *   - `patente`: The uppercase and correctly formatted license plate string if valid, otherwise `null`.
     *   - `error`: A string describing the validation result: "Valid License Plate" if valid, "Invalid patente" otherwise.
     */
    fun validatePatente(newValue: String): StringValidatorResult {
        val uppercaseValue = newValue.uppercase()
        val validValue = uppercaseValue.matches(Regex("[A-Z]{2}[0-9]{3}[A-Z]{2}|[A-Z]{3}[0-9]{3}"))
        val formattedValue = if(validValue) uppercaseValue  else  ""
        return if (validValue) {
            StringValidatorResult(
                isValid = true,
                value = formattedValue,
                errorMessage = "Valid License Plate"
            )
        } else {
            StringValidatorResult(isValid = false, value = null, errorMessage = "Invalid patente")
        }
    }
}