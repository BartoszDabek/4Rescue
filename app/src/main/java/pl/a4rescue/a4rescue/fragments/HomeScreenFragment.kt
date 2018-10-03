package pl.a4rescue.a4rescue.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.api.ResolvableApiException
import kotlinx.android.synthetic.main.fragment_home_screen.*
import pl.a4rescue.a4rescue.R
import pl.a4rescue.a4rescue.activities.CrashDetectingActivity
import pl.a4rescue.a4rescue.util.LocationService


class HomeScreenFragment : Fragment(), FragmentDrawerCheck {

    private val TAG = HomeScreenFragment::class.java.simpleName
    private val location: LocationService = LocationService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated")


        startBtn.setOnClickListener {
            if (checkLocationPermission()) {
                val startLocationRequests = location.startLocationRequests(activity!!)

                startLocationRequests?.addOnFailureListener(activity!!) { e ->
                    if (e is ResolvableApiException) {
                        Log.d(TAG, "Missing permissions!")
                        Log.d(TAG, "GPS DISABLED, NEED TO ASK FOR ENABLE")
                        e.startResolutionForResult(activity, LocationService.REQUEST_LOCATION_TURN_ON)

                    }
                }

                startLocationRequests?.addOnSuccessListener {
                    location.continueLocationRequests(activity!!)
                    Log.d(TAG, "GPS ENABLED, USER CAN PROCEED")
                    val intent = Intent(activity, CrashDetectingActivity::class.java)
                    startActivity(intent)

                }
            }
        }
        checkDrawer(activity!!, R.id.nav_home)
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(activity!!,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            askUserForPermission()
            return false
        } else {
            return true
        }
    }

    private fun askUserForPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
            AlertDialog.Builder(activity!!)
                    .setTitle(R.string.title_location_permission)
                    .setMessage(R.string.text_location_permission)
                    .setPositiveButton(R.string.ok) { dialogInterface, i ->
                        ActivityCompat.requestPermissions(activity!!,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                LocationService.REQUEST_LOCATION_PERMISSION)
                    }
                    .create()
                    .show()
        } else {
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LocationService.REQUEST_LOCATION_PERMISSION)
        }
    }
}