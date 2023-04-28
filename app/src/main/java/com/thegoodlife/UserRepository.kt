package com.thegoodlife

import androidx.lifecycle.MutableLiveData
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.jvm.Synchronized
/**
 * Manages all UserData.
 * Responsible for tracking the current user and storing previous/inactive users.
 * Provides mechanisms for updating the current user and switching to new/old users.
 */
class UserRepository private constructor(userDao: UserDao) {

    // LiveData object that stores the current user
    val mCurrUser = MutableLiveData<UserData?>()
    var mUserString: String? = null//LiveData<String>? = null// = /*Mutable*/LiveData<String>()

    private var mUserDao: UserDao = userDao

    // UserDao is/would-be the intermediary between the UserRepo and a database, implement only if time allows
    // private var mUserDao: UserDao = userDao // not yet implemented

    val allUserData: Flow<List<UserTable>> = userDao.getAllUser()

    private var mUsername: String? = null
    private var mUserData: String? = null //when is this set?

    /**
     * change the current user & update their entry in the database
     */
    fun updateCurrUser(updatedUser: UserData?){
        mScope.launch(Dispatchers.IO){
            if (updatedUser != null) {
                // update live data
                mCurrUser.postValue(updatedUser)
                // update database (TODO in REQ4)

                //println("IN USER REPOSITORY")
                update(updatedUser) // not yet implemented
            }
        }
    }

    fun fetchUserString(username: String): String// LiveData<String>// MutableLiveData<String>
    {
        //mUsername = username
/*
        mScope.launch(Dispatchers.IO) {
            //fetch data on a worker thread
            mUserString.postValue(username)

            //return getUserData(username)
        }

 */
        mUserString = getUserData(username)
        return mUserString as String// as LiveData<String>
    }

    fun switchToNewUser(){
        // TODO in REQ3
    }

    fun switchToExistingUser(/*take in some ID parameter, whatever is convenient w/ the database*/){
        // TODO in REQ3
    }

    fun deleteCurrentUser(){
        // TODO in REQ3
    }

    @WorkerThread
    /*suspend*/ fun getUserData(username: String): String?//LiveData<String>//MutableLiveData<String>
    {
        println(username)
        //var userdata: String? = null///*Mutable*/LiveData<String>? = null
        mScope.launch(Dispatchers.IO) {

            var userdata = mUserDao.getUserData(username)
            mUserData = userdata
        }
        //var userdata = mUserDao.getUserData(username)
        //mUserString = userdata!!
        return mUserData//userdata!!//.toString()
    }

    @WorkerThread
    suspend fun insert(newUser: UserData){
        // TODO in REQ4

        if (mUsername != null && mUserData != null) {
            //mUserDao.insert(UserTable(mUsername!!, mUserData!!))
            //mUserDao.insert(UserTable(mUsername!!, true, mUserData!!))

        }
    }

    @WorkerThread
    suspend fun update(user: UserData){//dao update takes in id, current, and data -> should just be current ==>> get whatever has curr = true,
        // TODO in REQ4
        /*
        if (user != null)
        {//mUsername != null && mUserData != null) {
            mUserDao.update(user)//mUsername!!, mUserData!!) //was usertable(,)
        }
         */
        //

        println("IN UREP - WORKER THREAD")
        mUsername = user.username
        mUserData = user.name + "|" + user.age.toString() + "|" + user.weight.toString() + "|" + user.height.toString() + "|" +  user.sex + "|" + user.activity_level + "|" + user.profile_pic_file_path + "|" +  user.country + "|" + user.city
        mUserDao.update(mUsername!!, mUserData!!)

    }

    companion object {
        private var mInstance: UserRepository? = null
        private lateinit var mScope: CoroutineScope
        @Synchronized
        fun getInstance(
            userDao: UserDao,
            scope: CoroutineScope
        ): UserRepository {
            mScope = scope
            return mInstance?: synchronized(this){
                val instance = UserRepository(userDao)
                mInstance = instance
                instance
            }
        }
    }
}