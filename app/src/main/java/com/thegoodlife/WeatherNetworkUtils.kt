package com.thegoodlife

import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.Throws
object WeatherNetworkUtils {
    private const val BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q="
    private const val APPIDQUERY = "&appid="
    private const val app_id = "d983add27e8dacc9efd5c6dad9f21250" // Free-Tier API key, limited risk in public visibility

    @JvmStatic
    fun buildURLFromString(location: String): URL? {
        var myURL: URL? = null
        try {
            myURL = URL(BASE_URL + location + APPIDQUERY + app_id)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return myURL
    }

    @JvmStatic
    @Throws(IOException::class)
    fun getDataFromURL(url: URL): String? {
        val urlConnection = url.openConnection() as HttpURLConnection
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
}