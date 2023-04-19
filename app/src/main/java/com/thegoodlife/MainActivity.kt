package com.thegoodlife

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.thegoodlife.UserCreateFragment.ReceiveUserInterface


class MainActivity : AppCompatActivity(), ReceiveUserInterface {

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
        mUserViewModel.currUser
        mWeatherViewModel.weather

        if(savedInstanceState == null) {
            //Create the fragment
            val userCreateFragment = UserCreateFragment()

            //Replace the fragment container
            val fTrans = supportFragmentManager.beginTransaction()
            fTrans.replace(R.id.fl_frag_container, userCreateFragment, "userCreat_frag")
            fTrans.commit()
        }
    }

    override fun receiveUserProfile(data: UserData?) {
        // receive a bundle with user data
        mUser = data
        if(!mUser?.name.isNullOrBlank())
            Toast.makeText(this, "Welcome, %s".format(mUser?.name), Toast.LENGTH_SHORT).show()

        // replace the header fragment
        // make new fragment
        val headerFragment = HeaderFragment()
        val args = Bundle()
        args.putParcelable("User", mUser)
        headerFragment.arguments = args
        // switch to fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.fl_header_container, headerFragment, "header_frag")
        transaction?.addToBackStack(null)
        transaction?.commit()
    }





    companion object {
        private var mUser: UserData? = null
    }
}