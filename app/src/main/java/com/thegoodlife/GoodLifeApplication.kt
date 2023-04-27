package com.thegoodlife

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
class GoodLifeApplication : Application(){
    //Get a global scope for all coroutines
    val applicationScope = CoroutineScope(SupervisorJob())

    // Inject scope and application context into singleton database
    // val userDB by lazy{ UserRoomDatabase.getDatabase(this,applicationScope)}

    // Inject database dao and scope into singleton repository. We
    // maintain a single global scope used for all coroutine operations in the repository and db.
    // If the viewmodel needs to spin up coroutines for some inconceivable reason, you can use
    // viewmodelscope inside the viewmodel
    val userRepository by lazy{ UserRepository.getInstance(UserRoomDatabase.userDao(),applicationScope)}
    val weatherRepository by lazy{ WeatherRepository.getInstance(applicationScope)}
}