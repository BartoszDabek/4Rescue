package pl.bdabek.a4rescue.activities

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.telephony.SmsManager
import android.util.Log
import pl.bdabek.a4rescue.R
import pl.bdabek.a4rescue.persistence.Contact
import pl.bdabek.a4rescue.util.LocationService
import pl.bdabek.a4rescue.viewmodel.ContactViewModel

class SendSMSActivity : AppCompatActivity() {

    private val TAG = SendSMSActivity::class.java.simpleName
    private val smsManager = SmsManager.getDefault()
    private lateinit var contactViewModel: ContactViewModel
    private val mapQuery = "http://maps.google.com/?q="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_sms)
        Log.d(TAG, "onCreate")
        //TODO: ask for permissions to send sms when asking for retrieving location

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        contactViewModel.allContacts.observe(this, Observer<List<Contact>> { contacts ->
            contacts?.forEach { contact ->
                run {
                    Log.d(TAG, "sending message to ${contact.phoneNumber}" )
                    val coordinates = "${LocationService.latitude},${LocationService.longitude}"
                    val userMapLocation = mapQuery + coordinates
                    val message = getString(R.string.sms_message) + userMapLocation
                    val dividedMessage = smsManager.divideMessage(message)
                    smsManager.sendMultipartTextMessage(contact.phoneNumber, null, dividedMessage, null, null)
                }
            }
        })
    }
}
