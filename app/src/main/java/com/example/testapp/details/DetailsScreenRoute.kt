package com.example.testapp.details

import com.example.testapp.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data class DetailsScreenRoute(
    val id: Long
) : Route
