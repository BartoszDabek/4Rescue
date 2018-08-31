package pl.a4rescue.a4rescue.fragments

import android.support.v4.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_main.*

interface FragmentDrawerCheck {
    fun checkDrawer(activity: FragmentActivity, drawerItem: Int) {
        val drawerMenu = activity.nav_view.menu
        val menuItem = drawerMenu.findItem(drawerItem)
        if (!menuItem.isChecked) {
            menuItem.isChecked = true
        }
    }
}