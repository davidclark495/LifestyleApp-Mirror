package com.thegoodlife

import android.graphics.Bitmap
import kotlin.math.round

data class UserData (
    var name: String?,
    var age: Int?,
    var weight: Int?,
    var height: Int?,
    var sex: String?,
    var activity_level: String?,
    var profile_pic: Bitmap?,
    var profile_pic_file_path: String?,
    var country: String?,
    var city: String?
){
    val bmr: Int
        get() = calcBMR()

    private fun calcBMR(): Int {
        val kgWeight: Double = weight!! * 0.45359237
        val cmHeight: Double = height!! * 2.54
        var BMRVal: Double
        if (sex == "Male") {
            BMRVal = round(((10 * (kgWeight)) + (6.25 * cmHeight) - (5 * age!!) + 5))
        }
        else { // same as "else (Female)"
            BMRVal = round(((10 * (kgWeight)) + (6.25 * cmHeight) - (5 * age!!) - 161))
        }
        // factor in activity level:
        var activityMultiplier = 1.0
        when (activity_level) {
            "Extra" ->      activityMultiplier = 1.9
            "Moderate" ->   activityMultiplier = 1.55
            "Light" ->      activityMultiplier = 1.375
            "Sedentary" ->  activityMultiplier = 1.2
        }
        BMRVal *= activityMultiplier

        return BMRVal.toInt()
    }
}