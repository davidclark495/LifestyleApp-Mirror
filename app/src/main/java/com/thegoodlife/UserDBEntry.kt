package com.thegoodlife
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDBEntry (
    @PrimaryKey(autoGenerate = true) val id: Int,
    var is_current: Boolean?,
    var name: String?,
    var age: Int?,
    var weight: Int?,
    var height: Int?,
    var sex: String?,
    var activity_level: String?,
    var profile_pic_file_path: String?,
    var country: String?,
    var city: String?
)