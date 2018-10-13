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
import pl.a4rescue.a4rescue.util.PermissionManager


class HomeScreenFragment : Fragment(), FragmentDrawerCheck {

    private val TAG = HomeScreenFragment::class.java.simpleName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated")

        startBtn.setOnClickListener {
            if (PermissionManager.checkPermissions(activity!!)) {
                val startLocationRequests = LocationService.prepareLocation(activity!!)

                startLocationRequests?.addOnFailureListener(activity!!) { e ->
                    if (e is ResolvableApiException) {
                        Log.d(TAG, "Missing permissions!")
                        Log.d(TAG, "GPS DISABLED, NEED TO ASK FOR ENABLE")
                        e.startResolutionForResult(activity, LocationService.REQUEST_LOCATION_TURN_ON)

                    }
                }

                startLocationRequests?.addOnSuccessListener {
                    LocationService.startLocationRequests(activity!!.applicationContext)
                    Log.d(TAG, "GPS ENABLED, USER CAN PROCEED")
                    val intent = Intent(activity, CrashDetectingActivity::class.java)
                    startActivity(intent)

                }
            }
        }
        checkDrawer(activity!!, R.id.nav_home)
    }
}