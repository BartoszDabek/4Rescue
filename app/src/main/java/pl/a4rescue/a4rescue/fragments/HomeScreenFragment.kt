package pl.a4rescue.a4rescue.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home_screen.*
import pl.a4rescue.a4rescue.R
import pl.a4rescue.a4rescue.activities.CrashListenerActivity

class HomeScreenFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        startBtn.setOnClickListener {
            val intent = Intent(activity, CrashListenerActivity::class.java)
            startActivity(intent)
        }
    }

}