package pl.bdabek.a4rescue.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "contacts")
data class Contact (
        @ColumnInfo(name = "user_name")
        val userName: String,

        @PrimaryKey
        @ColumnInfo(name = "phone_number")
        val phoneNumber: String
)
