package pl.a4rescue.a4rescue.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


object LocationService {

    private val TAG = LocationService::class.java.simpleName
    const val REQUEST_LOCATION_TURN_ON: Int = 1
    const val REQUEST_LOCATION_PERMISSION: Int = 2

    private var locationCallback: LocationCallback? = null
    private val locationRequest = LocationRequest()
    var longitude: Double? = null
    var latitude: Double? = null

    init {
        Log.d(TAG, "Init Location Service")
        locationCallback = MyLocationCallback()
    }

    @SuppressLint("MissingPermission")
    fun prepareLocation(activity: Activity): Task<LocationSettingsResponse>? {
        Log.d(TAG, "prepareLocation")
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val settingsBuilder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

        return LocationServices.getSettingsClient(activity.applicationContext)
                .checkLocationSettings(settingsBuilder.build())
    }

    @SuppressLint("MissingPermission")
    fun startLocationRequests(context: Context) {
        Log.d(TAG, "startLocationRequests")
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context.applicationContext)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun stopLocationRequests(context: Context) {
        Log.d(TAG, "stopLocationRequests")
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context.applicationContext)
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    class MyLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                longitude = location.longitude
                latitude = location.latitude
                Log.d(TAG, "SERVICE LONGITUDE: $longitude")
                Log.d(TAG, "SERVICE LATITUDE: $latitude")
            }
        }
    }

}