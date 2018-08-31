package pl.a4rescue.a4rescue.activities

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_crash_detecting.*
import pl.a4rescue.a4rescue.R
import pl.a4rescue.a4rescue.listeners.ShakeEventListener

class CrashDetectingActivity : AppCompatActivity() {

    private val TAG = CrashDetectingActivity::class.java.simpleName
    private var mSensorManager: SensorManager? = null
    private var mSensorListener: ShakeEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_crash_detecting)

        configureShakeSensor()

        stopBtn.setOnClickListener {
            stopMeasureGForces()
            switchToMainActivity()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        startMeasureGForces()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        stopMeasureGForces()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
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
