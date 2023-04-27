package com.thegoodlife

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

//class UserTable(s: String, s1: String) {

    @Entity(tableName = "user_table")
    data class UserTable(

        //us
        @PrimaryKey var username: String, //hidden ID
        //@ColumnInfo(name="")

        //var userdata: String,

        var name: String,
        var age: Int,
        var weight: Int,
        var height: Int,
        var activity: String,
        var sex: String,
        var country: String,
        var city: String



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