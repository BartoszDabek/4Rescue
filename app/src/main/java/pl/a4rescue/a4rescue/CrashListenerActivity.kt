package pl.a4rescue.a4rescue

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.crash_listener_activity.*

class CrashListenerActivity : AppCompatActivity() {

    private var mSensorManager: SensorManager? = null
    private var mSensorListener: ShakeEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crash_listener_activity)

        startMeasureGForces()
        stopBtnClickListener()
    }

    private fun startMeasureGForces() {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorListener = ShakeEventListener()
        registerSensorListener()
        executeActionWhenGForceIsToHigh()
    }

    private fun registerSensorListener() {
        mSensorManager?.registerListener(mSensorListener,
                mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI)
    }

    private fun executeActionWhenGForceIsToHigh() {
        mSensorListener!!.setOnShakeListener(object : ShakeEventListener.OnShakeListener {
            override fun onShake() {
                Toast.makeText(this@CrashListenerActivity, "Shake!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun stopBtnClickListener() {
        stopBtn.setOnClickListener {
            unregisterSensorListener()
            switchToMainActivity()
        }
    }

    private fun unregisterSensorListener() {
        mSensorManager?.unregisterListener(mSensorListener)
    }

    private fun switchToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "ON RESUME CrashListenerActivity")
        registerSensorListener()
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "ON PAUSE CrashListenerActivity")
        unregisterSensorListener()
    }

    override fun onStop() {
        super.onStop()
        Log.d("TAG", "ON STOP CrashListenerActivity")
        unregisterSensorListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "ON DESTROY CrashListenerActivity")
    }
}
