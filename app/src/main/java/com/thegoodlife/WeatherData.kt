package com.thegoodlife

import android.os.Parcel
import android.os.Parcelable

class WeatherData : Parcelable{
    //Setters and Getters
    var currentCondition: CurrentCondition = CurrentCondition()
    var temperature: Temperature = Temperature()
    var wind: Wind = Wind()
    var rain: Rain = Rain()
    var snow: Snow = Snow()
    var clouds: Clouds = Clouds()
    var locationData: LocationData? = null


    constructor() {}
    inner class CurrentCondition {
        var weatherId: Long = 0
        var condition: String? = null
        var descr: String? = null
        var icon: String? = null
        var pressure = 0.0
        var humidity = 0.0
    }

    inner class Temperature {
        var temp: Double = 0.0
        var minTemp: Double = 0.0
        var maxTemp: Double = 0.0
    }

    inner class Wind {
        var speed = 0.0
        var deg = 0.0
    }

    inner class Rain {
        var time: String? = null
        var amount = 0.0
    }

    inner class Snow {
        var time: String? = null
        var amount = 0.0
    }

    inner class Clouds {
        var perc: Long = 0
    }

    inner class LocationData {
        var latitude = 0.0
        var longitude = 0.0
        var country: String? = null
        var city: String? = null
        var sunset: Long = 0
        var sunrise: Long = 0
    }

    // "Parcelable" stuff from in-class Ex. 22 //
    //Say how and what to write to parcel
    override fun writeToParcel(out: Parcel, flags: Int) {
        // current condition
        out.writeLong(currentCondition.weatherId)
        out.writeString(currentCondition.condition)
        out.writeString(currentCondition.descr)
        out.writeString(currentCondition.icon)
        out.writeDouble(currentCondition.pressure)
        out.writeDouble(currentCondition.humidity)
        // temperature
        out.writeDouble(temperature.temp)
        out.writeDouble(temperature.minTemp)
        out.writeDouble(temperature.maxTemp)
        // wind
        out.writeDouble(wind.speed)
        out.writeDouble(wind.deg)
        // rain
        out.writeString(rain.time)
        out.writeDouble(rain.amount)
        // snow
        out.writeString(snow.time)
        out.writeDouble(snow.amount)
        // clouds
        out.writeLong(clouds.perc)
        // location data (if null, write junk data)
        out.writeDouble(locationData?.latitude ?: 9999.0)
        out.writeDouble(locationData?.longitude ?: 9999.0)
        out.writeString(locationData?.country)
        out.writeString(locationData?.city)
        out.writeLong(locationData?.sunset ?: -1)
        out.writeLong(locationData?.sunrise ?: -1)
    }

    //Say how to read in from parcel
    private constructor(`in`: Parcel) {
        // current condition
        currentCondition.weatherId = `in`.readLong()
        currentCondition.condition = `in`.readString()
        currentCondition.descr = `in`.readString()
        currentCondition.icon = `in`.readString()
        currentCondition.pressure = `in`.readDouble()
        currentCondition.humidity = `in`.readDouble()
        // temperature
        temperature.temp = `in`.readDouble()
        temperature.minTemp = `in`.readDouble()
        temperature.maxTemp = `in`.readDouble()
        // wind
        wind.speed = `in`.readDouble()
        wind.deg = `in`.readDouble()
        // rain
        rain.time = `in`.readString()
        rain.amount = `in`.readDouble()
        // snow
        snow.time = `in`.readString()
        snow.amount = `in`.readDouble()
        // clouds
        clouds.perc = `in`.readLong()
        // location data (may be junk data; only save if meaningful)
        val tempLocationData = LocationData()
        tempLocationData.latitude = `in`.readDouble()
        tempLocationData.longitude = `in`.readDouble()
        tempLocationData.country = `in`.readString()
        tempLocationData.city = `in`.readString()
        tempLocationData.sunset = `in`.readLong()
        tempLocationData.sunrise = `in`.readLong()
        /*
         * only do a partial validity check (latitude != 9999) to guarantee
         * the original WeatherData's location data wasn't null
         */
        val tempLocationIsMeaningful = tempLocationData.latitude != 9999.0
        if (tempLocationIsMeaningful){
            locationData = tempLocationData
        }
    }

    //Don't worry about this for now, not sure why we need it, sorry
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherData> {
        override fun createFromParcel(parcel: Parcel): WeatherData {
            return WeatherData(parcel)
        }
        override fun newArray(size: Int): Array<WeatherData?> {
            return arrayOfNulls(size)
        }
    }


}