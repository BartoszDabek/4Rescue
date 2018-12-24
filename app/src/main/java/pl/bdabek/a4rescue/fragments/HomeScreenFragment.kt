package pl.bdabek.a4rescue.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ResolvableApiException
import kotlinx.android.synthetic.main.fragment_home_screen.*
import pl.bdabek.a4rescue.R
import pl.bdabek.a4rescue.activities.CrashDetectingActivity
import pl.bdabek.a4rescue.util.LocationService
import pl.bdabek.a4rescue.util.PermissionManager
import pl.bdabek.a4rescue.util.PermissionManager.Companion.LOCATION_REQUEST


class HomeScreenFragment : Fragment(), AbstractFragment {

    private val TAG = HomeScreenFragment::class.java.simpleName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated")

        startBtn.setOnClickListener { _ ->
            Log.d(TAG, "User clicked START")
            if (PermissionManager.checkPermissions(activity!!)) {
                Log.d(TAG, "Permissions granted")
                val startLocationRequests = LocationService.prepareLocation(activity!!)

                startLocationRequests?.addOnFailureListener(activity!!) { e ->
                    if (e is ResolvableApiException) {
                        Log.d(TAG, "GPS DISABLED, NEED TO ASK FOR ENABLE")
                        e.startResolutionForResult(activity, LOCATION_REQUEST)
                    }
                }

                startLocationRequests?.addOnSuccessListener {
                    LocationService.startLocationRequests(activity!!)
                    Log.d(TAG, "GPS ENABLED, USER CAN PROCEED")
                    val intent = Intent(activity, CrashDetectingActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        checkDrawer(activity!!, R.id.nav_home)
    }
}