package com.example.navigatour

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.example.navigatour.databinding.ActivityMainBinding
import com.example.navigatour.utils.LocationPermissionHelper
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity() {
    private var routesMarked = ArrayList<String>()
    var currentLongFromClickListener: Double = 0.0
    var currentLatFromClickListener: Double = 0.0

    private lateinit var binding: ActivityMainBinding
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

    //Spawn a default clicker at the users location, then when the user clicks add coordinate, a marker will
    //be added where they signified
    private val onMapClickListener = object : OnMapClickListener {

        override fun onMapClick(point: Point): Boolean {
             currentLongFromClickListener = point.longitude()
             currentLatFromClickListener = point.latitude()

            binding.selectedLat.text = "%.4f".format(point.latitude())
            binding.selectedLong.text = "%.4f".format(point.longitude())

            return false
        }
    }

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        getUsersLocationPermission()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        mapView = binding.mapViewArea

        //asking for location permission
        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {
            onMapReady()
        }

        binding.addCoord.setOnClickListener{
            addMarkerFromClick()
        }

    }

    //Initialize Maps
    private fun onMapReady() {

//        val pointAnnotationManager = annotationApi?.createAnnotationManager(mapView)
//        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions().withPoint(
//            Point.fromLngLat(18.06, 59.31)).withIconImage()



//        var point: Int
//        mapView.getMapboxMap().addOnMapClickListener(point -> {
//            val currentLocation = point.latitude()
//            val currentLong = point.longitude()
//            val currentAlt = point.altitude()
//        })

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
    }

    //Adds movement listener
    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
        mapView.gestures.addOnMapClickListener(onMapClickListener)
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.mapbox_user_puck_icon,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    this@MainActivity,
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



    private fun addMarkerFromClick(){
            val annotationApi = mapView.annotations
            val circleAnnotationManager = annotationApi?.createCircleAnnotationManager()
            val circleAnnotationOptions: CircleAnnotationOptions = CircleAnnotationOptions().withPoint(Point.fromLngLat(currentLongFromClickListener, currentLatFromClickListener))
                // Style the circle that will be added to the map.
                .withCircleRadius(8.0)
                .withCircleColor("#ee4e8b")
                .withCircleStrokeWidth(2.0)
                .withCircleStrokeColor("#ffffff")
                .withDraggable(true)
            circleAnnotationManager?.create(circleAnnotationOptions)

        routesMarked.add("$currentLongFromClickListener,$currentLatFromClickListener")
        Log.d("array", routesMarked.toString())
    }
}