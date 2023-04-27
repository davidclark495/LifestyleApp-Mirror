package com.thegoodlife

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [UserDBEntry::class], version = 1, exportSchema = false)
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
            userDao.insert(
                UserDBEntry(
                    id = 1,
                    is_current = false,  // not the current user
                    name = "TEST_NAME",
                    age = 60,
                    weight = 180,
                    height = 68,
                    sex = "Female",
                    activity_level = "Sedentary",
                    profile_pic_file_path = null,
                    country = "Uruguay",
                    city = "La Paz"
                )
            )
        }
    }
}

//@Database(entities = [WeatherTable::class], version = 1, exportSchema = false)
//abstract class WeatherRoomDatabase : RoomDatabase() {
//    abstract fun weatherDao(): WeatherDao
//
//    // Make the db singleton. Could in theory
//    // make this an object class, but the companion object approach
//    // is nicer (imo)
//    companion object {
//        @Volatile
//        private var mInstance: WeatherRoomDatabase? = null
//        fun getDatabase(
//            context: Context,
//            scope : CoroutineScope
//        ): WeatherRoomDatabase {
//            return mInstance?: synchronized(this){
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    WeatherRoomDatabase::class.java, "weather.db"
//                )
//                    .addCallback(RoomDatabaseCallback(scope))
//                    .fallbackToDestructiveMigration()
//                    .build()
//                mInstance = instance
//                instance
//            }
//        }
//
//        private class RoomDatabaseCallback(
//            private val scope: CoroutineScope
//        ): RoomDatabase.Callback() {
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                mInstance?.let { database ->
//                    scope.launch(Dispatchers.IO){
//                        populateDbTask(database.weatherDao())
//                    }
//                }
//            }
//        }
//
//        suspend fun populateDbTask (weatherDao: WeatherDao) {
//            weatherDao.insert(WeatherTable("Dummy_loc","Dummy_data"))
//        }
//    }
//}