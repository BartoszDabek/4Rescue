package pl.a4rescue.a4rescue

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startBtn.setOnClickListener {
            val intent = Intent(this, CrashListenerActivity::class.java)
            startActivity(intent)
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
