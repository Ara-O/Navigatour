package com.example.navigatour

import java.util.*

data class RouteLegs(
    val via_waypoints: List<Objects>,
    val admins: List<Objects>,
    val weight: Double,
    val duration: Double,
    val steps: List<Objects>,
    val distance: Double,
    val summary: String
)

data class Coords(val coordinates: List<Array<Double>>)
data class RoutesData(
    val country_crossed: Boolean,
    val weight_name: String,
    val weight: Double,
    val duration: Double,
    val distance: Double,
    val legs: List<RouteLegs>,
    val geometry: Coords
)
