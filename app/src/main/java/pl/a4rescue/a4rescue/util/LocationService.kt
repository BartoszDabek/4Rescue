package pl.a4rescue.a4rescue.util

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


object LocationService {

    private val TAG = LocationService::class.java.simpleName
    const val REQUEST_LOCATION_TURN_ON: Int = 1
    const val REQUEST_LOCATION_PERMISSION: Int = 2

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null
    private val locationRequest = LocationRequest()
    var longitude: Double? = null
    var latitude: Double? = null

    init {
        locationCallback = MyLocationCallback()
    }

    @SuppressLint("MissingPermission")
    fun startLocationRequests(activity: Activity): Task<LocationSettingsResponse>? {
        Log.d(TAG, "startLocationRequests")
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val settingsBuilder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

        return LocationServices.getSettingsClient(activity)
                .checkLocationSettings(settingsBuilder.build())
    }

    @SuppressLint("MissingPermission")
    fun continueLocationRequests(activity: Activity) {
        Log.d(TAG, "continueLocationRequests")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun stopLocationUpdates() {
        Log.d(TAG, "stopLocationUpdates")
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    class MyLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                longitude = location.longitude
                latitude = location.latitude
                Log.d("SERVICE LONGITUDE: ", "$longitude")
                Log.d("SERVICE LATITUDE: ", "$latitude")
            }
        }
    }

}