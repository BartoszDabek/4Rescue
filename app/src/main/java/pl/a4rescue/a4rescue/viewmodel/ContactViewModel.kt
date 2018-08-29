package pl.a4rescue.a4rescue.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.util.Log
import android.widget.Toast
import pl.a4rescue.a4rescue.R
import pl.a4rescue.a4rescue.persistence.ContactRepository
import pl.a4rescue.a4rescue.persistence.Contact


class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = ContactViewModel::class.java.simpleName
    private val appContext: Application
    private val mRepository: ContactRepository = ContactRepository(application)
    private val CONTACT_LIMIT = 3

    internal val allWords: LiveData<List<Contact>>

    init {
        allWords = mRepository.contacts
        appContext = application
    }

    fun insert(contact: Contact) {
        if (maxContactsReached()) {
            Log.d(TAG, "Can not insert contact. Already ${allWords.value?.size} defined")
            Toast.makeText(appContext.applicationContext, appContext.getString(R.string.max_contacts), Toast.LENGTH_SHORT).show()
        } else {
            Log.d(TAG, "Inserting: $contact")
            mRepository.insert(contact)
        }
    }

    private fun maxContactsReached() = allWords.value?.size!! >= CONTACT_LIMIT

    fun delete(contact: Contact) {
        Log.d(TAG, "Deleting: $contact")
        mRepository.delete(contact)
    }
}