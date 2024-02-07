package com.huhn.architecturetemplate.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.huhn.architecturetemplate.navigation.MainNavGraph
import com.huhn.architecturetemplate.ui.theme.ArchitectureTemplateTheme

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLocation : Location? = null

    var isLocationPermissionGranted = false
    private val locationPermissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            var msg = "Location permission must be granted to run app"
            when {
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    defaultValue = false
                ) -> {
                    // Precise location access granted.
                    isLocationPermissionGranted = true
                    msg = "Location permission granted"
                }

                permissions.getOrDefault(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    defaultValue = false
                ) -> {
                    // Only approximate location access granted.
                }

                else -> {
                    // No location access granted.
                }
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askLocationPermissions()

        setContent {
            ArchitectureTemplateTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavGraph()
                }
            }
        }
    }
    fun askLocationPermissions() {
        // Before performing the actual permission request,
        // check whether app already has the permissions,
        if (!isLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted, ask for it
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else {
                isLocationPermissionGranted = true
            }
        }

        if (isLocationPermissionGranted) {
            Toast.makeText(this, "Location permission granted", Toast.LENGTH_LONG).show()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    // Got last known location. In some rare situations this can be null.
                    lastLocation = location
                }
        } else {
            Toast.makeText(this, "Location permission must be granted to run app", Toast.LENGTH_LONG)
                .show()
        }
    }
}
