package com.example.navigatour

import java.util.*


data class RoutesData(
    val country_crossed: Boolean,
    val distance: Double,
    val duration: Double,
    val geometry: Geometry,
    val legs: List<Leg>,
    val weight: Double,
    val weight_name: String
)

data class Waypoint(
    val distance: Double,
    val location: List<Double>,
    val name: String
)

data class Geometry(
    val coordinates: List<List<Double>>,
    val type: String
)

data class Leg(
    val admins: List<Admin>,
    val distance: Double,
    val duration: Double,
    val steps: List<Step>,
    val summary: String,
    val via_waypoints: List<Any>,
    val weight: Double
)

data class Admin(
    val iso_3166_1: String,
    val iso_3166_1_alpha3: String
)

data class Step(
    val distance: Double,
    val driving_side: String,
    val duration: Double,
    val geometry: Geometry,
    val intersections: List<Intersection>,
    val maneuver: Maneuver,
    val mode: String,
    val name: String,
    val weight: Double
)

data class Intersection(
    val admin_index: Int,
    val bearings: List<Int>,
    val duration: Double,
    val entry: List<Boolean>,
    val geometry_index: Int,
    val `in`: Int,
    val is_urban: Boolean,
    val location: List<Double>,
    val mapbox_streets_v8: MapboxStreetsV8,
    val `out`: Int,
    val weight: Double
)

data class Maneuver(
    val bearing_after: Int,
    val bearing_before: Int,
    val instruction: String,
    val location: List<Double>,
    val modifier: String,
    val type: String
)

data class MapboxStreetsV8(
    val `class`: String
)
data class RouteDirections (
    val maneuver: List<RouteManeuvers>
)

data class RouteManeuvers(
    val instruction: String
)

data class Coords(val coordinates: List<Array<Double>>)
//data class RoutesData(
//    val country_crossed: Boolean,
//    val weight_name: String,
//    val weight: Double,
//    val duration: Double,
//    val distance: Double,
//    val legs: List<RouteLegs>,
//    val geometry: Coords
//)
