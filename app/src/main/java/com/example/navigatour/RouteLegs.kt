package com.example.navigatour

import java.util.Objects

data class RouteLegs(
    val via_waypoints: List<Objects>,
    val admins: List<Objects>,
    val weight: Double,
    val duration: Double,
    val steps: List<Objects>,
    val distance: Double,
    val summary: String

)
