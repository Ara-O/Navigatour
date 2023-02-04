package com.example.navigatour

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RoutesApi {
    @GET("directions/v5/mapbox/walking/{chosenRouteMarkers}?access_token=pk.eyJ1IjoiYXJhLW8iLCJhIjoiY2xkaGhhYnhmMTdnaDN3cjh4azFqc2QwNyJ9.HOF3rCh-G66AFpjz6al8ew")
    suspend fun getRoutes(@Path("chosenRouteMarkers") chosenRouteMarkers: String): Response<Routes>
}