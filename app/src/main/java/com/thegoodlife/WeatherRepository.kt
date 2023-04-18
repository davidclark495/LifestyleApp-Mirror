package com.thegoodlife

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

/**
 * Manages all WeatherData.
 * Responsible for fetching, parsing, and storing data.
 * Provides mechanisms for accessing and mutating stored data.
 */
// add WeatherDao class / database functionality, only if time allows
class WeatherRepository private constructor() {

    // holds the most recently queried WeatherData
    val mWeather = MutableLiveData<WeatherData>()

    fun updateWeatherAtLocation(location: String){
        mScope.launch(Dispatchers.IO){
            val weatherJSON = fetchWeatherData(location)
            if (weatherJSON != null) {
                val weatherObj = parseWeatherJSON(weatherJSON)
                if (weatherObj != null) {
                    // update live data
                    mWeather.postValue(weatherObj!!)
                }
            }
        }
    }

    @WorkerThread
    @Throws(IOException::class)
    suspend fun fetchWeatherData(location: String): String? {
        val url = buildURLFromString(location)
        val urlConnection = url?.openConnection() as HttpURLConnection
        return try {
            val inputStream = urlConnection.inputStream

            //The scanner trick: search for the next "beginning" of the input stream
            //No need to user BufferedReader
            val scanner = Scanner(inputStream)
            scanner.useDelimiter("\\A")
            val hasInput = scanner.hasNext()
            if (hasInput) {
                scanner.next()
            } else {
                null
            }
        } finally {
            urlConnection.disconnect()
        }
    }

    @WorkerThread
    suspend fun parseWeatherJSON(weatherJSON: String): WeatherData? {
        val weatherData = WeatherData()

        //Start parsing JSON data
        val jsonObject: JSONObject
        try {
            jsonObject = JSONObject(weatherJSON) // may throw JSONException
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }

        // get current condition
        val currentCondition = weatherData.currentCondition
        val jsonWeather = jsonObject.getJSONArray("weather").getJSONObject(0)
        currentCondition.condition = jsonWeather.getString("main")
        currentCondition.descr = jsonWeather.getString("description")
        val jsonMain = jsonObject.getJSONObject("main")
        currentCondition.humidity = jsonMain.getInt("humidity").toDouble()
        currentCondition.pressure = jsonMain.getInt("pressure").toDouble()
        weatherData.currentCondition = currentCondition

        // get temperature
        val temperature = weatherData.temperature
        temperature.maxTemp = jsonMain.getDouble("temp_max")
        temperature.minTemp = jsonMain.getDouble("temp_min")
        temperature.temp = jsonMain.getDouble("temp")
        weatherData.temperature = temperature

        // get wind
        val wind = weatherData.wind
        val jsonWind = jsonObject.getJSONObject("wind")
        wind.speed = jsonWind.getDouble("speed")
        wind.deg = jsonWind.getInt("deg").toDouble()
        weatherData.wind = wind

        val clouds = weatherData.clouds
        val jsonClouds = jsonObject.getJSONObject("clouds")
        clouds.perc = (jsonClouds.getInt("all") / 100.0).toLong()
        weatherData.clouds = clouds

        return weatherData
    }

    private fun buildURLFromString(location: String): URL? {
        val BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q="
        val APPIDQUERY = "&appid="
        val APP_ID = "d983add27e8dacc9efd5c6dad9f21250" // Free-Tier API key, limited risk in public visibility

        var myURL: URL? = null
        try {
            myURL = URL(BASE_URL + location + APPIDQUERY + APP_ID)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return myURL
    }








    companion object {
        private var mInstance: WeatherRepository? = null
        private lateinit var mScope: CoroutineScope
        @Synchronized
        fun getInstance(
            scope: CoroutineScope
        ): WeatherRepository {
            mScope = scope
            return mInstance?: synchronized(this){
                val instance = WeatherRepository()
                mInstance = instance
                instance
            }
        }
    }
}