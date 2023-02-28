package com.thegoodlife

import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator
import android.graphics.Bitmap
class User : Parcelable{
    private var mName: String? = null
    private var mAge: Int? = null
    private var mWeight: Int? = null
    private var mHeight: Int? = null
    private var mSex: String? = null
    private var mActivityLevel: String? = null
    private var mProfilePic: Bitmap? = null

    //update for location(?), use in data modification / loading frag

    constructor(
        name: String?,
        age: Int?,
        weight: Int?,
        height: Int?,
        sex: String?,
        activity_level: String?,
        profile_pic: Bitmap?
    ){
        mName = name
        mAge = age
        mWeight = weight
        mHeight = height
        mSex = sex
        mActivityLevel = activity_level
        mProfilePic = profile_pic
    }

    //Implement getters / setters
    val name: String?
        get() = mName
    val age: Int?
        get() = mAge
    val weight: Int?
        get() = mWeight
    val height: Int?
        get() = mHeight
    val sex: String?
        get() = mSex
    val activity_level: String?
        get() = mActivityLevel
    val profile_pic: Bitmap?
        get() = mProfilePic

    fun setName(name: String?){ mName = name }
    fun setAge(age: Int?){ mAge = age }
    fun setWeight(weight: Int?){ mWeight = weight }
    fun setHeight(height: Int?){ mHeight = height }
    fun setSex(sex: String?){ mSex = sex }
    fun setActivityLevel(activity_level: String?){ mActivityLevel = activity_level }
    fun setProfilePic(profile_pic: Bitmap?){ mProfilePic = profile_pic }

    // "Parcelable" stuff from in-class Ex. 22

    //Say how and what to write to parcel
    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeString(mName!!)
        out.writeInt(mAge!!)
        out.writeInt(mWeight!!)
        out.writeInt(mHeight!!)
        out.writeString(mSex!!)
        out.writeString(mActivityLevel!!)
        out.writeParcelable(mProfilePic!!, flags)
    }

    //Say how to read in from parcel
    private constructor(`in`: Parcel) {
        mName = `in`.readString() // may need !! on 'name', other fields
        mAge = `in`.readInt()
        mWeight = `in`.readInt()
        mHeight = `in`.readInt()
        mSex = `in`.readString()
        mActivityLevel = `in`.readString()
        mProfilePic = `in`.readParcelable(null, Bitmap::class.java)
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