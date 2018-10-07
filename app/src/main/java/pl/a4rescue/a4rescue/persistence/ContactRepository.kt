package pl.a4rescue.a4rescue.persistence

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log


class ContactRepository internal constructor(application: Application) {

    private val TAG = ContactRepository::class.java.simpleName
    private val contactDao: ContactDao
    internal val contacts: LiveData<List<Contact>>

    init {
        Log.d(TAG, "ContactRepository...init")
        val db = ContactDatabase.getInstance(application)
        contactDao = db.contactDao()
        contacts = contactDao.getAll()
    }

    fun insert(contact: Contact) {
        InsertAsyncTask(contactDao).execute(contact)
    }

    fun delete(contact: Contact) {
        DeleteAsyncTask(contactDao).execute(contact)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: ContactDao) : AsyncTask<Contact, Void, Void>() {
        override fun doInBackground(vararg params: Contact): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

    private class DeleteAsyncTask internal constructor(private val mAsyncTaskDao: ContactDao) : AsyncTask<Contact, Void, Void>() {
        override fun doInBackground(vararg params: Contact): Void? {
            mAsyncTaskDao.delete(params[0])
            return null
        }
    }
}