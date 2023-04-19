package com.thegoodlife

class WeatherData() {
    //Setters and Getters
    var currentCondition: CurrentCondition = CurrentCondition()
    var temperature: Temperature = Temperature()
    var wind: Wind = Wind()
    var rain: Rain = Rain()
    var snow: Snow = Snow()
    var clouds: Clouds = Clouds()
    var locationData: LocationData? = null


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
}