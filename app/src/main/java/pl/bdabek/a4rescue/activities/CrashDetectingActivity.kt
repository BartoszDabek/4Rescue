package pl.bdabek.a4rescue.activities

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_crash_detecting.*
import pl.bdabek.a4rescue.R
import pl.bdabek.a4rescue.listeners.ShakeEventListener
import pl.bdabek.a4rescue.util.LocationService

class CrashDetectingActivity : AppCompatActivity() {

    private val TAG = CrashDetectingActivity::class.java.simpleName
    private var mSensorManager: SensorManager? = null
    private var mSensorListener: ShakeEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_crash_detecting)

        LocationService.stopLocationRequests(this)
        configureShakeSensor()

        stopBtn.setOnClickListener {
            stopMeasureGForces()
            switchToMainActivity()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        stopMeasureGForces()
    }

    private fun configureShakeSensor() {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorListener = ShakeEventListener()
        startMeasureGForces()
        listenOnTooHighGForces()
    }

    private fun startMeasureGForces() {
        Log.d(TAG, "startMeasureGForces")
        mSensorManager?.registerListener(mSensorListener,
                mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI)
    }

    private fun listenOnTooHighGForces() {
        Log.d(TAG, "listenOnTooHighGForces")
        mSensorListener!!.setOnShakeListener(object : ShakeEventListener.OnShakeListener {
            override fun onShake() {
                Log.d(TAG, "Collision detected!")
                stopMeasureGForces()
                val intent = Intent(applicationContext, CrashDetectedActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun stopMeasureGForces() {
        Log.d(TAG, "stopMeasureGForces")
        mSensorManager?.unregisterListener(mSensorListener)
    }

    private fun switchToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
