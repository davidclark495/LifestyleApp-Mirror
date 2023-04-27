package com.thegoodlife

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import kotlin.jvm.Volatile
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [UserTable::class], version = 1, exportSchema = false)
abstract class UserRoomDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    // Make the db singleton. Could in theory
    // make this an object class, but the companion object approach
    // is nicer (imo)
    companion object {
        @Volatile
        private var mInstance: UserRoomDatabase? = null
        fun getDatabase(
            context: Context,
            scope : CoroutineScope
        ): UserRoomDatabase {
            return mInstance?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserRoomDatabase::class.java, "user.db"
                )
                    .addCallback(RoomDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                mInstance = instance
                instance
            }
        }

        private class RoomDatabaseCallback(
            private val scope: CoroutineScope
        ): RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                mInstance?.let { database ->
                    scope.launch(Dispatchers.IO){
                        populateDbTask(database.userDao())
                    }
                }
            }
        }

        suspend fun populateDbTask (userDao: UserDao) {
            userDao.insert(UserTable("Dummy_loc","Dummy_data"))//too many arguments? -> table.java stub
        }

        /*
        fun userDao(): UserDao {
            return userDao()
        }
        */
    }

}
