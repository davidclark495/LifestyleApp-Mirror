package com.thegoodlife

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WeatherViewModel(repository: WeatherRepository) : ViewModel() {

    // Connect a live data object to the current bit of weather info
    private val mWeather: LiveData<WeatherData> = repository.mWeather
    // Returns the data contained in the live data object
    val weather: LiveData<WeatherData>
        get() = mWeather

    private var mWeatherRepository: WeatherRepository = repository

    //Use a second live data here to show entire contents of db
    // This casts a flow in the repo as a live data so an observer in the view
    // can watch it. If you want to observe variables in the repo from the viewmodel, use
    // observeForever (not recommended as it's almost never needed)
    //
    // TODO, if time allows (currently weather doesn't have a database)
//    val allCityWeather: LiveData<List<WeatherFragment>> = repository.allCityWeather.asLiveData()

    fun setLocation(location: String) {
        // Simply pass the location to the repository
        mWeatherRepository.updateWeatherAtLocation(location)
    }

}

// This factory class allows us to define custom constructors for the view model
class WeatherViewModelFactory(private val repository: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}