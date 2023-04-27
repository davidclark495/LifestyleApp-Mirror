package com.thegoodlife

import androidx.room.*

import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    // Insert ignore
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userTable: UserTable)

    // Delete all
    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM user_table WHERE username LIKE :user LIMIT 1")
    suspend fun getUserData(user: String): UserTable //String has setter issues

    //need functionality for create, switch, update, delete

    @Query("DELETE FROM user_table WHERE username LIKE :user")
    suspend fun deleteUser(user: String)

    //or

    @Query("UPDATE user_table SET userdata = :user_data WHERE username LIKE :user_name")
    suspend fun update(user_name: String, user_data: String)

    /*
    @Delete
    fun delete(user: String)
    */

    // Get all the weather info that is currently in the database
    // automatically triggered when the db is updated because of Flow<List<WeatherTable>>
    @Query("SELECT * from user_table where lower(userdata)!='dummy_data' ORDER BY userdata")
    fun getAllUser(): Flow<List<UserTable>>
}