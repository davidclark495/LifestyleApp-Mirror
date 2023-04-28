package com.thegoodlife

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

//class UserTable(s: String, s1: String) {

    @Entity(tableName = "user_table")
    data class UserTable(

        //@PrimaryKey(autoGenerate = true) val id: Int,
        //var iscurrent: String,
        @PrimaryKey var username: String,
        var userdata: String,

        //@PrimaryKey var username: String,
        //var userdata: String,

        //or just bool, data, and idno
/*
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


 */

        /*
        @field:ColumnInfo(name = "username")
        @field:PrimaryKey var username: String,



        @field:ColumnInfo(
            name = "userdata" //everything but pic probably -> should use UserData object -> 'unsure how to store (json is fine)

            //, val height: String = ""// weight etc.

        ) /*var userdata: UserData*/ var userJson: String = ""

         */
)

//}