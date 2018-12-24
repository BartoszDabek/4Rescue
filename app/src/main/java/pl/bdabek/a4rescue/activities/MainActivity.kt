package pl.bdabek.a4rescue.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ResolvableApiException
import kotlinx.android.synthetic.main.activity_main.*
import pl.bdabek.a4rescue.R
import pl.bdabek.a4rescue.fragments.ContactFragment
import pl.bdabek.a4rescue.fragments.HomeScreenFragment
import pl.bdabek.a4rescue.util.LocationService
import pl.bdabek.a4rescue.util.PermissionManager.Companion.LOCATION_REQUEST
import pl.bdabek.a4rescue.util.PermissionManager.Companion.RESCUE_PERMISSIONS


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val location: LocationService = LocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        setupDrawerToggle()
        setupDrawerContent()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeScreenFragment())
                    .commit()
            nav_view.setCheckedItem(R.id.nav_home)
        }
    }

    private fun setupDrawerToggle() {
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    private fun setupDrawerContent() {
        nav_view.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        lateinit var fragment: Fragment

        when (menuItem.itemId) {
            R.id.nav_home -> fragment = HomeScreenFragment()
            R.id.nav_contacts -> fragment = ContactFragment()
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()

        drawer_layout.closeDrawers()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LOCATION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "USER AGREED ON TURN ON LOCATION")
                    startLocationRequestAndSwitchActivity()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d(TAG, "onRequestPermissionsResult")
        when (requestCode) {
            RESCUE_PERMISSIONS -> {
                checkPermissionsAndAskForLocationRequest(grantResults)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun checkPermissionsAndAskForLocationRequest(grantResults: IntArray) {
        val location = 0
        val sendSms = 1
        if (grantResults[location] == PackageManager.PERMISSION_GRANTED &&
                grantResults[sendSms] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "Permission granted for ${Manifest.permission.ACCESS_FINE_LOCATION} and ${Manifest.permission.SEND_SMS}")
                askForLocationRequest()
            }
        }
    }

    fun askForLocationRequest() {
        val startLocationRequests = location.prepareLocation(this)

        startLocationRequests?.addOnFailureListener(this) { e ->
            if (e is ResolvableApiException) {
                Log.d(TAG, "LOCATION DISABLED, NEED TO ASK FOR ENABLE")
                e.startResolutionForResult(this, LOCATION_REQUEST)
            }
        }

        startLocationRequests?.addOnSuccessListener {
            Log.d(TAG, "LOCATION ENABLED, USER CAN PROCEED")
            startLocationRequestAndSwitchActivity()
        }
    }

    fun startLocationRequestAndSwitchActivity() {
        Log.d(TAG, "startLocationRequestAndSwitchActivity")
        location.startLocationRequests(applicationContext)
        val intent = Intent(this, CrashDetectingActivity::class.java)
        startActivity(intent)
    }
}
