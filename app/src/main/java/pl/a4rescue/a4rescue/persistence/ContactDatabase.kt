package pl.a4rescue.a4rescue.persistence

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import android.util.Log


@Database(entities = [Contact::class], version = 1)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {
        private val TAG = ContactDatabase::class.java.simpleName
        private val dbName = "contact.db"
        private var INSTANCE: ContactDatabase? = null

        fun getInstance(context: Context): ContactDatabase {
            if (INSTANCE == null) {
                synchronized(ContactDatabase::class) {
                    Log.d(TAG, "Creating db instance")
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            ContactDatabase::class.java, dbName)
                            .build()
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            Log.d(TAG, "Destroying db instance")
            INSTANCE = null
        }
    }

}