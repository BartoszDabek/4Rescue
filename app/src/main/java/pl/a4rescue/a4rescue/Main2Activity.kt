package pl.a4rescue.a4rescue

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    private var mSensorManager: SensorManager? = null
    private var mSensorListener: ShakeEventListener? = null
    private val fileSaver = FileSaver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorListener = ShakeEventListener()
        mSensorManager?.registerListener(mSensorListener,
                mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI)
        mSensorListener!!.setOnShakeListener(object : ShakeEventListener.OnShakeListener {

            override fun onShake() {
                Toast.makeText(this@Main2Activity, "Shake!", Toast.LENGTH_SHORT).show()
            }
        })

        fileSaver.init(this)

        stopBtn.setOnClickListener {
            mSensorManager?.unregisterListener(mSensorListener)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "ON RESUME Main2Activity")
        mSensorManager?.registerListener(mSensorListener,
                mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "ON PAUSE Main2Activity")
        mSensorManager?.unregisterListener(mSensorListener)
    }

    override fun onStop() {
        super.onStop()
        Log.d("TAG", "ON STOP Main2Activity")
        mSensorManager?.unregisterListener(mSensorListener)
        fileSaver.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "ON DESTROY Main2Activity")
    }
}
