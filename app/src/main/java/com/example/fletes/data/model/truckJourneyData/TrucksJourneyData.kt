package com.example.fletes.data.model.truckJourneyData

import com.example.fletes.data.model.DecimalTextFieldData

data class TruckJourneyData(
    val camionId: Int,
    val kmCargaData: DecimalTextFieldData,
    val kmDescargaData: DecimalTextFieldData,
    val kmSurtidorData: DecimalTextFieldData,
    val litrosData: DecimalTextFieldData,
    val isActive: Boolean,
)
