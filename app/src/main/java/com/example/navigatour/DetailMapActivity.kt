package com.example.navigatour

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.example.navigatour.databinding.ActivityDetailedMapBinding
import com.example.navigatour.utils.LocationPermissionHelper
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class DetailMapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedMapBinding

    private lateinit var mapView: MapView
    private lateinit var locationPermissionHelper: LocationPermissionHelper

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = binding.mapViewDetailedArea
        //asking for location permission
        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {
            onMapReady()
        }

    }


    private fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        mapView.getMapboxMap().loadStyleUri(
            Style.SATELLITE
        ) {
            initLocationComponent()
            setupGesturesListener()
        }

        mapView.buildLayer()

        val routeMarkers = intent.getStringExtra("routeMarkers")
        if (routeMarkers != null) {
            displayPolyline(routeMarkers)
        }
    }


    //Adds movement listener
    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }

    //Keeping track of user location
    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    this@DetailMapActivity,
                    R.drawable.mapbox_user_puck_icon,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    this@DetailMapActivity,
                    R.drawable.mapbox_user_icon_shadow,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
    }


    private fun onCameraTrackingDismissed() {
        Toast.makeText(this, "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }


    override fun onDestroy() {
        super.onDestroy()
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun displayPolyline(chosenRouteMarkers: String){
        val routesApi = RetrofitHelper.getInstance().create(RoutesApi::class.java)

        var geometryPoints = java.util.ArrayList<Point>()
//        val gg = ArrayList<Point>()
        GlobalScope.launch {
            val result = routesApi.getRoutes(chosenRouteMarkers)
            Log.d("api data", result.toString())
            if(result != null){
                for(i in result.body()!!.routes[0].geometry.coordinates.indices) {
                    val subArray = result.body()!!.routes[0].geometry.coordinates[i]
                    for (j in subArray.indices) {
                        //create points from here
                        //Log.d("sup-end", "index $j - ${subArray[j]}")
                    }
                    geometryPoints += Point.fromLngLat(subArray[0], subArray[1])
                }

                var listOfSteps = ""

                //looping through each step and adding it to the list of steps
                for(steps in result.body()!!.routes[0].legs[0].steps){
                    Log.d("steps", steps.maneuver.instruction)
                    var step = steps.maneuver.instruction
                    listOfSteps+= step
                    listOfSteps += ";"
                }

                displaySteps(listOfSteps)

                runOnUiThread(Runnable {
                    // Perform your map operation here
                    val annotationApi = mapView?.annotations
                    val polylineAnnotationManager = annotationApi?.createPolylineAnnotationManager()
                    // Set options for the resulting line layer.
                    val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
                        .withPoints(geometryPoints)
                        // Style the line that will be added to the map.
                        .withLineColor("#add8e6")
                        .withLineWidth(5.0)
                    // Add the resulting line to the map.
                    polylineAnnotationManager?.create(polylineAnnotationOptions)

                    Log.d("detailMapActivity", "should be displaying line")

                })


            } else {
                Log.d("MainActivity", "An error occured")
            }
        }
    }

    private fun displaySteps(listOfSteps: String){
        runOnUiThread(Runnable {
            // Perform your map operation here
        val stepsDataArray: List<String> = listOfSteps?.split(";")!!

        for(step in stepsDataArray){
            binding.stepsList.append(step)
            binding.stepsList.append("\n")
        }
        })
    }
}