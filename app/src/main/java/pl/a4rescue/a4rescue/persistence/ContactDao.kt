package pl.a4rescue.a4rescue.persistence

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Flowable


@Dao
interface ContactDao {

    @Query("SELECT * FROM contacts")
    fun getAll(): LiveData<List<Contact>>

    @Insert(onConflict = IGNORE)
    fun insert(contact: Contact)

    @Delete
    fun delete(contact: Contact)
}