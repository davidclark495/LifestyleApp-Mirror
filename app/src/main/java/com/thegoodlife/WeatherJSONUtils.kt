package com.thegoodlife

import kotlin.Throws
import org.json.JSONException
import org.json.JSONObject

//Declare methods as static. We don't want to create objects of this class.
object WeatherJSONUtils {
    @Throws(JSONException::class)
    fun getWeatherData(data: String?): WeatherData {
        val weatherData = WeatherData()

        //Start parsing JSON data
        val jsonObject = JSONObject(data!!) //Must throw JSONException
        val currentCondition = weatherData.currentCondition
        val jsonMain = jsonObject.getJSONObject("main")
        currentCondition.humidity = jsonMain.getInt("humidity").toDouble()
        currentCondition.pressure = jsonMain.getInt("pressure").toDouble()
        weatherData.currentCondition = currentCondition

        //Get the temperature, wind and cloud data.
        val temperature = weatherData.temperature
        val wind = weatherData.wind
        val clouds = weatherData.clouds
        temperature.maxTemp = jsonMain.getDouble("temp_max")
        temperature.minTemp = jsonMain.getDouble("temp_min")
        temperature.temp = jsonMain.getDouble("temp")
        weatherData.temperature = temperature
        return weatherData
    }
}