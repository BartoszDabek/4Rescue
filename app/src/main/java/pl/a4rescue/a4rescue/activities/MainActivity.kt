package pl.a4rescue.a4rescue.activities

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import pl.a4rescue.a4rescue.R
import pl.a4rescue.a4rescue.fragments.ContactFragment
import pl.a4rescue.a4rescue.fragments.HomeScreenFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        nav_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        HomeScreenFragment()).commit()
                R.id.nav_contacts -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        ContactFragment()).commit()
            }
            drawer_layout.closeDrawers()

            true
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    HomeScreenFragment()).commit()
            nav_view.setCheckedItem(R.id.nav_home)
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "ON RESUME MainActivity")
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "ON PAUSE MainActivity")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TAG", "ON STOP MainActivity")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "ON DESTROY MainActivity")
    }

}
