package com.thegoodlife

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData

class UserViewModel(repository: UserRepository) : ViewModel() {

    // Connect a live data object to the current bit of weather info
    private val mCurrUser: LiveData<UserData?> = repository.mCurrUser
    private var mUserRepository: UserRepository = repository

    private var mUserData: String? = null//LiveData<String>? = repository.mUserString

    //Use a second live data here to show entire contents of db
    // This casts a flow in the repo as a live data so an observer in the view
    // can watch it. If you want to observe variables in the repo from the viewmodel, use
    // observeForever (not recommended as it's almost never needed)
    //


    // TODO in REQ4
//    val allCityWeather: LiveData<List<WeatherFragment>> = repository.allCityWeather.asLiveData()

    var allUsers: LiveData<List<UserTable>> = repository.allUserData.asLiveData()

    fun updateCurrUser(updatedUser: UserData?){
        // TODO
        println("IN USER VIEW MODEL")
        mUserRepository.updateCurrUser(updatedUser)
    }

    fun getUserData(username: String): String?//LiveData<String> {
    {
        println("--$username--")
        return mUserRepository.getUserData(username)
        //mUserData = mUserRepository.fetchUserString(username)
        //return mUserData as String //as LiveData<String>
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

    // Returns the data contained in the live data object
    val currUser: LiveData<UserData?>
        get() = mCurrUser
}

// This factory class allows us to define custom constructors for the view model
class UserViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}