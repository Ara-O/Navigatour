package com.example.navigatour

data class RoutesData(
    val country_crossed: Boolean,
    val weight_name: String,
    val weight: Double,
    val duration: Double,
    val distance: Double,
    val legs: List<RouteLegs>,
    val geometry: Object
)
