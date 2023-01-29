package com.example.navigatour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.navigatour.databinding.ActivityMainBinding
import com.mapbox.maps.MapView
import com.mapbox.maps.Style

var mapView: MapView? = null

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        mapView = binding.mapView
        mapView?.getMapboxMap()?.loadStyleUri(Style.MAPBOX_STREETS)

    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }


    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }
}