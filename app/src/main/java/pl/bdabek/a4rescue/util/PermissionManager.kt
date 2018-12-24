package pl.bdabek.a4rescue.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.util.Log
import pl.bdabek.a4rescue.R

class PermissionManager {

    companion object {
        const val LOCATION_REQUEST: Int = 1
        const val RESCUE_PERMISSIONS: Int = 2

        fun checkPermissions(activity: Activity): Boolean {
            Log.d("PermissionManager", "checkPermissions in $activity")
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                Log.d("PermissionManager", "Permissions not granted")
                askUserForPermission(activity)
                return false
            } else {
                Log.d("PermissionManager", "Permissions granted")
                return true
            }
        }

        private fun askUserForPermission(activity: Activity) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS)) {

                displayPermissionInfo(activity, R.string.text_app_permissions)
            } else {
                displayPermissionInfo(activity, R.string.text_app_permissions)
            }
        }

        private fun displayPermissionInfo(activity: Activity, message: Int) {
            AlertDialog.Builder(activity)
                    .setTitle(R.string.title_app_permissions)
                    .setMessage(message)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        ActivityCompat.requestPermissions(
                                activity,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS),
                                RESCUE_PERMISSIONS)
                    }
                    .create()
                    .show()
        }

    }
}