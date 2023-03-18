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
}