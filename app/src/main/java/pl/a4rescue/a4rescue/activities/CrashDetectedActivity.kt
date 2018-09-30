package pl.a4rescue.a4rescue.activities

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_crash_detected.*
import pl.a4rescue.a4rescue.R
import pl.a4rescue.a4rescue.util.LocationService


class CrashDetectedActivity : AppCompatActivity() {

    companion object {
        private const val COUNTDOWN_TIMER_LENGTH = 20
    }

    private val TAG = CrashDetectedActivity::class.java.simpleName
    private val location: LocationService = LocationService
    private var originalMusicVolume: Int = 0
    private lateinit var audioManager: AudioManager
    private lateinit var vibrator: Vibrator
    private lateinit var alarm: MediaPlayer
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_crash_detected)

        getUserCurrentLocation()
        setUpAndStartTimer()
        turnOnVibration()
        turnOnAlarm()

        swipe_btn.setOnActiveListener {
            Log.d(TAG, "Stopping alarm and vibration")
            location.stopLocationUpdates()
            turnOffVibrationAndAlarm()
            timer.cancel()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUserCurrentLocation() {
        location.continueLocationRequests(this)
    }

    private fun setUpAndStartTimer() {
        progress_countdown.max = COUNTDOWN_TIMER_LENGTH

        timer = object : CountDownTimer(22000, 1000) {
            override fun onFinish() {
                Log.d(TAG, "LONGITUDE: ${location.longitude}")
                Log.d(TAG, "LATITUDE: ${location.latitude}")
                //TODO: switch intent to crash not cancelled + send notifications to defined users
//                location.stopLocationUpdates()
            }

            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000) - 1
                updateCountdownUI(secondsRemaining)
            }
        }.start()
    }

    private fun updateCountdownUI(secondsRemaining: Long) {
        Log.d(TAG,"Seconds remaining $secondsRemaining")
        countdown_tv.text = secondsRemaining.toString()
        progress_countdown.progress = (COUNTDOWN_TIMER_LENGTH - secondsRemaining).toInt()

        if (secondsRemaining == 0L) {
            progress_countdown.progress = 100
            turnOffVibrationAndAlarm()
        }
    }

    private fun turnOnVibration() {
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrationPattern = longArrayOf(0, 400, 1000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "Vibration on API >= 26")
            val effect = VibrationEffect.createWaveform(vibrationPattern, 0)
            vibrator.vibrate(effect)
        } else {
            Log.d(TAG, "Vibration on API < 26")
            @Suppress("DEPRECATION")
            vibrator.vibrate(vibrationPattern, 0)
        }
    }

    private fun turnOnAlarm() {
        val alarmSoundURI = getAlarmSound()
        alarm = MediaPlayer.create(applicationContext, alarmSoundURI)
        alarm.isLooping = true
        setSoundAtMaxVolume()
        alarm.start()
    }

    private fun getAlarmSound(): Uri {
        val sirenSoundResource = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                packageName + "/" + R.raw.siren_sound
        return Uri.parse(sirenSoundResource)
    }

    private fun setSoundAtMaxVolume() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        originalMusicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
    }

    private fun turnOffVibrationAndAlarm() {
        vibrator.cancel()
        alarm.stop()
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalMusicVolume, 0)
    }
}
