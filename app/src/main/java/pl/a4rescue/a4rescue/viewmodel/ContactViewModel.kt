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
    private val repository: ContactRepository = ContactRepository(application)
    private val CONTACT_LIMIT = 3

    internal val allContacts: LiveData<List<Contact>>

    init {
        Log.d(TAG, "ContactViewModel...init")
        allContacts = repository.contacts
        appContext = application
    }

    fun insert(contact: Contact) {
        if (maxContactsReached()) {
            Log.d(TAG, "Can not insert contact. Already ${allContacts.value?.size} defined")
            Toast.makeText(appContext.applicationContext, appContext.getString(R.string.max_contacts), Toast.LENGTH_SHORT).show()
        } else {
            Log.d(TAG, "Inserting: $contact")
            repository.insert(contact)
        }
    }

    private fun maxContactsReached() = allContacts.value?.size!! >= CONTACT_LIMIT

    fun delete(contact: Contact) {
        Log.d(TAG, "Deleting: $contact")
        repository.delete(contact)
    }
}