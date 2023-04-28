package com.thegoodlife

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    // Initialize the user/weather view models (also, inject the respective repositories).
    // We define our own constructor + our own view model factory in each viewmodel's .kt file.
    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as GoodLifeApplication).userRepository)
    }
    private val mWeatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory((application as GoodLifeApplication).weatherRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // important: access the view models to cause them to lazily-load NOW
        // (otherwise fragments may want to create their own copies)
        // (definitely a hack, but it's easy)
        mUserViewModel.currUser
        mWeatherViewModel.weather

        if(savedInstanceState == null) {

            //signin / signup fragment :
            //signin asks for username -> queries -> goes to

            //or have currentuser boolean like david did: select from
/*
            //Create the fragment
            val userCreateFragment = UserCreateFragment()

            //Replace the fragment container
            val fTrans = supportFragmentManager.beginTransaction()
            fTrans.replace(R.id.fl_frag_container, userCreateFragment, "userCreat_frag")
            fTrans.commit()
*/
            val loginScreenFragment = LoginScreenFragment()

            //Replace the fragment container
            val fTrans = supportFragmentManager.beginTransaction()
            fTrans.replace(R.id.fl_login_container, loginScreenFragment, "loginScreen_frag")
            fTrans.commit()
        }
    }
}