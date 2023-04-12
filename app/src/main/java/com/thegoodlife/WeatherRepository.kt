package com.thegoodlife

import androidx.lifecycle.MutableLiveData
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.jvm.Synchronized

/**
 * Manages all WeatherData.
 * Responsible for fetching, parsing, and storing data.
 * Provides mechanisms for accessing and mutating stored data.
 */
// add WeatherDao class / database functionality, only if time allows
class WeatherRepository private constructor(/*weatherDao: WeatherDao*/) {

    // holds the most recently queried WeatherData
    val mWeather = MutableLiveData<WeatherData>()
    // WeatherDao is/would-be the intermediary between the WeatherRepo and a database, implement only if time allows
    // private var mWeatherDao: WeatherDao = weatherDao

    fun updateWeatherAtLocation(location: String){
        mScope.launch(Dispatchers.IO){
            val weatherJSON = fetchWeatherData(location)
            if (weatherJSON != null) {
                val weatherObj = parseWeatherJSON(weatherJSON)
                if (weatherObj != null) {
                    // update live data
                    mWeather.postValue(weatherObj!!)
                    // update database
                    //insert(location, weatherJSON) // not yet implemented
                }
            }
        }
    }

    @WorkerThread
    suspend fun fetchWeatherData(location: String): String? {
        return null
    }

    @WorkerThread
    suspend fun parseWeatherJSON(weatherJSON: String): WeatherData? {
        return null // dummy weather data
    }

    // not necessary: only create a weather database if time allows
//    @WorkerThread
//    suspend fun insert(location: String, weatherJSON: String){
//        if (location != null && weatherJSON != null) {
//            //
//        }
//    }

    companion object {
        private var mInstance: WeatherRepository? = null
        private lateinit var mScope: CoroutineScope
        @Synchronized
        fun getInstance(
            // weatherDao: WeatherDao
            scope: CoroutineScope
        ): WeatherRepository {
            mScope = scope
            return mInstance?: synchronized(this){
                val instance = WeatherRepository(/*weatherDao*/)
                mInstance = instance
                instance
            }
        }
    }
}