package com.thegoodlife

import androidx.lifecycle.MutableLiveData
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.jvm.Synchronized
/**
 * Manages all UserData.
 * Responsible for tracking the current user and storing previous/inactive users.
 * Provides mechanisms for updating the current user and switching to new/old users.
 */
class UserRepository private constructor(/*userDao: UserDao*/) {

    // LiveData object that stores the current user
    val mCurrUser = MutableLiveData<UserData?>()

    // UserDao is/would-be the intermediary between the UserRepo and a database, implement only if time allows
    // private var mUserDao: UserDao = userDao // not yet implemented

    /**
     * change the current user & update their entry in the database
     */
    fun updateCurrUser(updatedUser: UserData?){
        mScope.launch(Dispatchers.IO){
            if (updatedUser != null) {
                // update live data
                mCurrUser.postValue(updatedUser)
                // update database (TODO in REQ4)
                //update(updatedUser) // not yet implemented
            }
        }
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
    suspend fun insert(newUser: UserData){
        // TODO in REQ4
    }

    @WorkerThread
    suspend fun update(user: UserData){
        // TODO in REQ4
    }

    companion object {
        private var mInstance: UserRepository? = null
        private lateinit var mScope: CoroutineScope
        @Synchronized
        fun getInstance(
//            userDao: UserDao
            scope: CoroutineScope
        ): UserRepository {
            mScope = scope
            return mInstance?: synchronized(this){
                val instance = UserRepository(/*userDao*/)
                mInstance = instance
                instance
            }
        }
    }
}