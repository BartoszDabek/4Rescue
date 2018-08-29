package pl.a4rescue.a4rescue.persistence

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask


class ContactRepository internal constructor(application: Application) {

    private val mContactDao: ContactDao
    internal val contacts: LiveData<List<Contact>>

    init {
        val db = ContactDatabase.getInstance(application)
        mContactDao = db.contactDao()
        contacts = mContactDao.getAll()
    }

    fun insert(contact: Contact) {
        InsertAsyncTask(mContactDao).execute(contact)
    }

    fun delete(contact: Contact) {
        DeleteAsyncTask(mContactDao).execute(contact)
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