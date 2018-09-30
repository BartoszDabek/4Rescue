package pl.a4rescue.a4rescue.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import pl.a4rescue.a4rescue.R
import pl.a4rescue.a4rescue.fragments.ContactFragment
import pl.a4rescue.a4rescue.fragments.HomeScreenFragment
import pl.a4rescue.a4rescue.util.LocationService
import pl.a4rescue.a4rescue.util.LocationService.REQUEST_LOCATION_PERMISSION
import pl.a4rescue.a4rescue.util.LocationService.REQUEST_LOCATION_TURN_ON


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val location: LocationService = LocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        setupDrawerToggle()
        setupDrawerContent()

        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        location.startLocationRequests(this)

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
            REQUEST_LOCATION_TURN_ON -> {
                if (resultCode == RESULT_CANCELED) {
                    Log.d(TAG, "USER DISAGREED ON TURN ON LOCATION")
                    location.startLocationRequests(this)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Log.d(TAG, "USER DISAGREED ON USING LOCALIZATION")
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
