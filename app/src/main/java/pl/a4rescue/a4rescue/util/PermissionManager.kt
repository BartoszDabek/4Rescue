package pl.a4rescue.a4rescue.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import pl.a4rescue.a4rescue.R

class PermissionManager {

    companion object {
        fun checkPermissions(activity: Activity): Boolean {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

                askUserForPermission(activity)
                return false
            } else {
                return true
            }
        }

        private fun askUserForPermission(activity: Activity) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                displayPermissionInfo(activity, R.string.text_location_permission)
            } else {
                displayPermissionInfo(activity, R.string.text_location_permission)
            }
        }

        private fun displayPermissionInfo(activity: Activity, message: Int) {
            AlertDialog.Builder(activity)
                    .setTitle(R.string.title_location_permission)
                    .setMessage(message)
                    .setPositiveButton(R.string.ok) { dialogInterface, i ->
                        ActivityCompat.requestPermissions(activity,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS),
                                LocationService.REQUEST_LOCATION_PERMISSION)
                    }
                    .create()
                    .show()
        }

    }
}