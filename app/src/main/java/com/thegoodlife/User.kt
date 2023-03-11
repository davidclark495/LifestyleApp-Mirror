package com.thegoodlife

import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator
import android.graphics.Bitmap
import android.os.Build
import kotlin.math.round

class User : Parcelable{
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
        var mBMRVal: Double
        if (mSex == "Male") {
            mBMRVal = round(((10 * (kgWeight)) + (6.25 * cmHeight) - (5 * mAge!!) + 5))
        }
        else { // same as "else (Female)"
            mBMRVal = round(((10 * (kgWeight)) + (6.25 * cmHeight) - (5 * mAge!!) - 161))
        }

        return mBMRVal.toInt()
    }

    // "Parcelable" stuff from in-class Ex. 22 //

    //Say how and what to write to parcel
    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeString(mName)
        out.writeInt(mAge ?: -1)        // mAge if not null, else -1
        out.writeInt(mWeight ?: -1)
        out.writeInt(mHeight ?: -1)
        out.writeString(mSex)
        out.writeString(mActivityLevel)
        out.writeParcelable(mProfilePic, flags)
        out.writeString(mProfilePicFilePath)
        out.writeString(mCountry)
        out.writeString(mCity)
    }

    //Say how to read in from parcel
    private constructor(`in`: Parcel) {
        mName = `in`.readString()
        mAge = `in`.readInt()
        mWeight = `in`.readInt()
        mHeight = `in`.readInt()
        mSex = `in`.readString()
        mActivityLevel = `in`.readString()
        if(Build.VERSION.SDK_INT >= 33) {
            mProfilePic = `in`.readParcelable(null, Bitmap::class.java)
        } else {
            mProfilePic = `in`.readParcelable(null)
        }
        mProfilePicFilePath = `in`.readString()
        mCountry = `in`.readString()
        mCity = `in`.readString()
    }

    //Don't worry about this for now, not sure why we need it, sorry
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }
        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }



}