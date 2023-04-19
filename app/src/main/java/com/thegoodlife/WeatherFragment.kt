package com.thegoodlife

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlin.math.roundToInt


private const val ARG_USER = "User"

/**
 * A simple [Fragment] subclass.
 * Significant sections are adapted from Example 27.
 */
class WeatherFragment : Fragment() {
    private lateinit var mUserViewModel: UserViewModel
    private lateinit var mWeatherViewModel: WeatherViewModel
    
    private var mUser: UserData? = null

    private var mLocationET: EditText? = null
    private var mWeatherData: WeatherData? = null
    private var mConditTV: TextView? = null
    private var mDescrTV: TextView? = null
    private var mTempTV: TextView? = null
    private var mWindSpeedTV: TextView? = null
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

        mLocationET = view.findViewById(R.id.et_location) as EditText
        mConditTV = view.findViewById(R.id.tv_condition) as TextView
        mDescrTV = view.findViewById(R.id.tv_description) as TextView
        mTempTV = view.findViewById(R.id.tv_temperature) as TextView
        mWindSpeedTV = view.findViewById(R.id.tv_windspeed) as TextView
        mPressureTV = view.findViewById(R.id.tv_pressure) as TextView
        mHumidTV = view.findViewById(R.id.tv_humidity) as TextView
        mSubmitButton = view.findViewById<View>(R.id.button_submit) as Button
        mSubmitButton!!.setOnClickListener {//Get the string from the edit text and sanitize the input
            val inputLocation = mLocationET!!.text.toString().replace(' ', '&')
            loadWeatherData(inputLocation)
        }

        // load the view models from the parent class
        mUserViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        mWeatherViewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
        mWeatherViewModel.weather.observe(requireActivity(), mLiveWeatherObserver)

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

    private val mLiveWeatherObserver: Observer<WeatherData> =
        Observer { weatherData -> // update the UI if this changes
            if (weatherData != null) {
                updateWeatherData(weatherData)
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        //outState.putParcelable("WeatherData", mWeatherData)
        super.onSaveInstanceState(outState)
    }

    private fun loadWeatherData(location: String) {
        // display a "loading" message
        val loadingMessage = "Loading..."
        mConditTV?.text     = loadingMessage
        mDescrTV?.text      = loadingMessage
        mTempTV?.text       = loadingMessage
        mWindSpeedTV?.text  = loadingMessage
        mHumidTV?.text      = loadingMessage
        mPressureTV?.text   = loadingMessage
        // fetch data
        mWeatherViewModel.setLocation(location)
    }

    /**
     *  updates mWeatherData and recalculates display values
     */
    fun updateWeatherData(data: WeatherData?){
        if(data == null){
            val waitingMessage = "[Submit to view Weather]"
            mConditTV?.text     = waitingMessage
            mDescrTV?.text      = waitingMessage
            mTempTV?.text       = waitingMessage
            mWindSpeedTV?.text  = waitingMessage
            mHumidTV?.text      = waitingMessage
            mPressureTV?.text   = waitingMessage
            return
        }

        mWeatherData = data
        mConditTV?.text =
            "" + mWeatherData?.currentCondition?.condition
        mDescrTV?.text =
            "" + mWeatherData?.currentCondition?.descr
        mTempTV?.text =
            "" + ((mWeatherData?.temperature?.temp?:0.0) - 273.15).roundToInt() + " C"
        mWindSpeedTV?.text =
            "" + (mWeatherData?.wind?.speed)?.roundToInt() + " m/sec"
        mHumidTV?.text =
            "" + mWeatherData?.currentCondition?.humidity + "%"
        mPressureTV?.text =
            "" + mWeatherData?.currentCondition?.pressure + " hPa"
    }
}