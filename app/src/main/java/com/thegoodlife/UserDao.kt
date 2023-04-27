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
    //called at the login page? -> unique

    //need functionality for create, switch, update, delete

    @Query("DELETE FROM user_table WHERE username LIKE :user")
    suspend fun deleteUser(user: String)

    //@Query("UPDATE user_table SET userdata = :user_data WHERE username LIKE :user_name")
    //@Query("INSERT INTO user_table (username, userdata) values (:user_name, :user_data) ON DUPLICATE KEY UPDATE username = values(:user_name), userdata = values(:user_data)")
    //@Query("INSERT OR IGNORE INTO user_table (username, userdata) values (:user_name, :user_data); UPDATE players SET username = :user_name, userdata = :user_data WHERE username = :user_name")

    //@Query("INSERT OR REPLACE INTO user_table (username, userdata) VALUES (:user_name, :user_data)")
    @Query("INSERT OR REPLACE INTO user_table (username, userdata) VALUES (:user_name, :user_data)")
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