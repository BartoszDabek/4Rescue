package pl.a4rescue.a4rescue.util

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*


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
    fun startLocationRequests(activity: Activity) {
        Log.d(TAG, "startLocationRequests")
        locationRequest.interval = 10 * 60 * 1000
        locationRequest.maxWaitTime = 60 * 60 * 1000
        locationRequest.fastestInterval = 2 * 60 * 1000
        locationRequest.priority = LocationRequest.PRIORITY_LOW_POWER

        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(activity)
        val task = client.checkLocationSettings(builder.build())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        task.addOnFailureListener(activity) { e ->
            if (e is ResolvableApiException) {
                Log.d(TAG, "Missing permissions!")
                e.startResolutionForResult(activity, REQUEST_LOCATION_TURN_ON)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun continueLocationRequests(activity: Activity) {
        Log.d(TAG, "continueLocationRequests")
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
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