package pl.bdabek.a4rescue.fragments

import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_main.*

interface AbstractFragment {

    fun checkDrawer(activity: FragmentActivity, drawerItem: Int) {
        val drawerMenu = activity.nav_view.menu
        val menuItem = drawerMenu.findItem(drawerItem)
        if (!menuItem.isChecked) {
            menuItem.isChecked = true
        }
    }

}