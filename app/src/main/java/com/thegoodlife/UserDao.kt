package com.thegoodlife

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    // Insert ignore
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userDBEntry: UserDBEntry)

    // Delete all
    @Query("DELETE FROM users")
    suspend fun deleteAll()

    // Get all the weather info that is currently in the database
    // automatically triggered when the db is updated because of Flow<List<WeatherTable>>
    @Query("SELECT * from users where lower(name)!='test_name' ORDER BY name")
    fun getAllUsers(): Flow<List<UserDBEntry>>
}
