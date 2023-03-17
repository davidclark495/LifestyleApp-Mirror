package com.thegoodlife

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.lang.ref.WeakReference
import java.util.concurrent.Executors
import androidx.core.os.HandlerCompat
import android.os.Looper
import org.json.JSONException
import kotlin.math.roundToInt


private const val ARG_USER = "User"

/**
 * A simple [Fragment] subclass.
 * Significant sections are adapted from Example 27.
 */
class WeatherFragment : Fragment() {
    private var mUser: UserData? = null

    private var mLocationET: EditText? = null
    private var mWeatherData: WeatherData? = null
    private var mTempTV: TextView? = null
    private var mPressureTV: TextView? = null
    private var mHumidTV: TextView? = null
    private var mSubmitButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if(Build.VERSION.SDK_INT >= 33) {
                mUser = it.getParcelable(ARG_USER, UserData::class.java)
            } else {
                mUser = it.getParcelable(ARG_USER)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weather, container, false)
        mFetchWeatherTask.setWeakReference(this) //make sure we're always pointing to current version of fragment

        mLocationET = view.findViewById(R.id.et_location) as EditText
        mTempTV = view.findViewById(R.id.tv_temperature) as TextView
        mPressureTV = view.findViewById(R.id.tv_pressure) as TextView
        mHumidTV = view.findViewById(R.id.tv_humidity) as TextView
        mSubmitButton = view.findViewById<View>(R.id.button_submit) as Button
        mSubmitButton!!.setOnClickListener {//Get the string from the edit text and sanitize the input
            val inputLocation = mLocationET!!.text.toString().replace(' ', '&')
            loadWeatherData(inputLocation)
        }

        // restore weather data from saved state, if possible
        if(savedInstanceState != null) {
            val savedWeatherData: WeatherData? = savedInstanceState.getParcelable("WeatherData")
            updateWeatherData(savedWeatherData)
        }

        // autofill Location w/ user data's location (or w/ blank data if lacking city/country)
        val userHasCityAndCountry = !(mUser?.city.isNullOrBlank()) && !(mUser?.country.isNullOrBlank())
        if (mUser != null && userHasCityAndCountry)
        {
            val userLocation = "%s, %s".format(mUser?.city,  mUser?.country)
            mLocationET?.setText(userLocation)
            if(savedInstanceState == null)
                loadWeatherData(userLocation)
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("WeatherData", mWeatherData)
        super.onSaveInstanceState(outState)
        Toast.makeText(requireContext(), "onSaveInstanceState", Toast.LENGTH_SHORT).show()
    }

    private fun loadWeatherData(location: String) {
        // display a "loading" message
        val loadingMessage = "Loading..."
        mTempTV?.text       = loadingMessage
        mHumidTV?.text      = loadingMessage
        mPressureTV?.text   = loadingMessage
        // fetch data
        mFetchWeatherTask.execute(location)
    }

    /**
     *  updates mWeatherData and recalculates display values
     */
    fun updateWeatherData(data: WeatherData?){
        if(data == null){
            val waitingMessage = "[Submit to view Weather]"
            mTempTV?.text       = waitingMessage
            mHumidTV?.text      = waitingMessage
            mPressureTV?.text   = waitingMessage
            return
        }
        mWeatherData = data
        mTempTV?.text =
            "" + ((mWeatherData?.temperature?.temp?:0.0) - 273.15).roundToInt() + " C"
        mHumidTV?.text =
            "" + mWeatherData?.currentCondition?.humidity + "%"
        mPressureTV?.text =
            "" + mWeatherData?.currentCondition?.pressure + " hPa"
    }

    // Background Thread Worker //
    // taken w/ modifications from Example 27
    private class FetchWeatherTask {
        var weatherFragmentWeakReference: WeakReference<WeatherFragment>? = null
        private val executorService = Executors.newSingleThreadExecutor()
        private val mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper())
        fun setWeakReference(ref: WeatherFragment) {
            weatherFragmentWeakReference = WeakReference(ref)
        }

        fun execute(location: String?) {
            executorService.execute {
                val weatherDataURL = WeatherNetworkUtils.buildURLFromString(location!!)
                try {
                    val jsonWeatherData = WeatherNetworkUtils.getDataFromURL(weatherDataURL!!)
                    postToMainThread(jsonWeatherData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        private fun postToMainThread(jsonWeatherData: String?) {
            val localRef = weatherFragmentWeakReference!!.get()
            mainThreadHandler.post {
                if (jsonWeatherData != null) {
                    try {
                        val newWeatherData = WeatherJSONUtils.getWeatherData(jsonWeatherData)
                        localRef!!.updateWeatherData(newWeatherData)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    companion object {
        private val mFetchWeatherTask = FetchWeatherTask()
    }
}