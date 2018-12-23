package pl.a4rescue.a4rescue.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query


@Dao
interface ContactDao {

    @Query("SELECT * FROM contacts")
    fun getAll(): LiveData<List<Contact>>

    @Insert(onConflict = IGNORE)
    fun insert(contact: Contact)

    @Delete
    fun delete(contact: Contact)
}