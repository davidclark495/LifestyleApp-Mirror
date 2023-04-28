package com.thegoodlife

import android.graphics.Bitmap
import kotlin.math.round

class UserData {
    private var mUsername: String? = null
    private var mName: String? = null
    private var mAge: Int? = null
    private var mWeight: Int? = null    // in pounds
    private var mHeight: Int? = null    // in inches
    private var mSex: String? = null    // "male" or "female"
    private var mActivityLevel: String? = null
    private var mProfilePic: Bitmap? = null
    private var mProfilePicFilePath: String? = null
    private var mCountry: String? = null
    private var mCity: String? = null

    //update for location(?), use in data modification / loading frag

    constructor(
        username: String?,
        name: String?,
        age: Int?,
        weight: Int?,
        height: Int?,
        sex: String?,
        activity_level: String?,
        profile_pic: Bitmap?,
        profile_pic_file_path: String?,
        country: String?,
        city: String?
    ){
        mUsername = username
        mName = name
        mAge = age
        mWeight = weight
        mHeight = height
        mSex = sex
        mActivityLevel = activity_level
        mProfilePic = profile_pic
        mProfilePicFilePath = profile_pic_file_path
        mCountry = country
        mCity = city
    }

    //Implement properties w/ getters & setters (use member variables as backing fields)
    var username: String?
        get() = mUsername
        set(value) {mUsername = value}
    var name: String?
        get() = mName
        set(value) { mName = value }
    var age: Int?
        get() = mAge
        set(value) { mAge = value }
    var weight: Int?
        get() = mWeight
        set(value) { mWeight = value }
    var height: Int?
        get() = mHeight
        set(value) { mHeight = value }
    var sex: String?
        get() = mSex
        set(value) { mSex = value }
    var activity_level: String?
        get() = mActivityLevel
        set(value) { mActivityLevel = value }
    var profile_pic: Bitmap?
        get() = mProfilePic
        set(value) { mProfilePic = value }
    var profile_pic_file_path: String?
        get() = mProfilePicFilePath
        set(value) { mProfilePicFilePath = value }
    var country: String?
        get() = mCountry
        set(value) { mCountry = value }
    var city: String?
        get() = mCity
        set(value) { mCity = value }
    val bmr: Int?
        get() = calcBMR()

    private fun calcBMR(): Int {
        val kgWeight: Double = mWeight!! * 0.45359237
        val cmHeight: Double = mHeight!! * 2.54
        var BMRVal: Double
        if (mSex == "Male") {
            BMRVal = round(((10 * (kgWeight)) + (6.25 * cmHeight) - (5 * mAge!!) + 5))
        }
        else { // same as "else (Female)"
            BMRVal = round(((10 * (kgWeight)) + (6.25 * cmHeight) - (5 * mAge!!) - 161))
        }
        // factor in activity level:
        var activityMultiplier: Double = 1.0
        when (mActivityLevel) {
            "Extra" ->      activityMultiplier = 1.9
            "Moderate" ->   activityMultiplier = 1.55
            "Light" ->      activityMultiplier = 1.375
            "Sedentary" ->  activityMultiplier = 1.2
        }
        BMRVal *= activityMultiplier

        return BMRVal.toInt()
    }
}