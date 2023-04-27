package com.thegoodlife

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

//class UserTable(s: String, s1: String) {

    @Entity(tableName = "user_table")
    data class UserTable(
        @field:ColumnInfo(name = "username")
        @field:PrimaryKey
        var username: String,
        @field:ColumnInfo(
            name = "userdata"
        ) var userJson: String
    )

//}