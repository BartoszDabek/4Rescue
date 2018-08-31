package pl.a4rescue.a4rescue.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import pl.a4rescue.a4rescue.R

class CrashDetectedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_detected)
    }
}
