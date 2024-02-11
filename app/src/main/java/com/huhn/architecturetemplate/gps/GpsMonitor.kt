package com.huhn.architecturetemplate.gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.huhn.architecturetemplate.utils.getActivity
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class GpsMonitor {
    companion object {
        private const val TAG = "EMH"

        fun fetchCurrentLocation(
            context: Context,
            onGpsUpdate: (Location) -> Unit
        ) {
            val isFineGranted = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            val isCoarseGranted = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (!isFineGranted || !isCoarseGranted) {
                onGpsUpdate(getNullLocation())
            } else {
                val activity = context.getActivity()
                activity?.let {
                    val fusedLocationClient =
                        LocationServices.getFusedLocationProviderClient(it)

//                    fusedLocationClient.lastLocation
//                        .addOnSuccessListener { location ->
//                            // Got last known location. In some rare situations this can be null.
//                            val lastLocation = if (location == null) {
//                                val msg = "Cannot get last location."
//                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
//                                getNullLocation()
//                            } else {
//                                val msg = "Last Location = [lat : ${location.latitude}, lng : ${location.longitude}]"
//                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
//                                Log.d(TAG, msg,)
//                                location
//                            }
//                            onGpsUpdate(lastLocation)
//                        }
//                        .addOnFailureListener { exception ->
//                            exception.printStackTrace()
//                            val msg = "Exception getting last location: ${exception.message}"
//                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
//                            Log.d(TAG, msg)
//                            onGpsUpdate(getNullLocation())
//                        }

                    val usePreciseLocation = true
                    val priority = if (usePreciseLocation) {
                        Priority.PRIORITY_HIGH_ACCURACY
                    } else {
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY
                    }
//                    val cancellationToken = CancellationTokenSource().token

                    val scope = CoroutineScope(Dispatchers.IO)
                    scope.launch {
//                    runBlocking {
                        val result = fusedLocationClient.getCurrentLocation(
                            priority,
                            null, // cancellationToken,
                        )
                        result.addOnSuccessListener { location: Location? ->
                            val currentLocation = if (location == null) {
                                val msg = "Cannot get location."
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                Log.e(TAG, msg)
                                getNullLocation()
                            } else {
                                location
                            }
                            onGpsUpdate(currentLocation)

                        }
                        result.addOnFailureListener { exception: Exception ->
                            exception.printStackTrace()
                            val msg = "Exception getting last location: ${exception.message}"
                            Log.d(TAG, msg)
                            onGpsUpdate(getNullLocation())
                        }
                    }

                }
            }
        }

        private fun getNullLocation(): Location {
            val tempLoc = Location("null location")
            tempLoc.latitude = 0.0
            tempLoc.longitude = 0.0
            return tempLoc
        }
    }
}