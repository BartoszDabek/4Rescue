package pl.a4rescue.a4rescue.persistence

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "contacts")
data class Contact (
        @ColumnInfo(name = "user_name")
        val userName: String,

        @PrimaryKey
        @ColumnInfo(name = "phone_number")
        val phoneNumber: String
)
