package com.example.fletes.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object CreateJourneyRoute

@Serializable
object CreateTruckRoute

@Serializable
object CreateDispatchRoute

@Serializable
object ActiveJourneysRoute

@Serializable
data object BuyDataRoute : java.io.Serializable // New route, ensure java.io.Serializable for objects

