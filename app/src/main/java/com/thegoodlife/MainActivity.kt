package com.thegoodlife

import androidx.appcompat.app.AppCompatActivity
import com.thegoodlife.UserCreateFragment.ReceiveUserInterface
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity(), ReceiveUserInterface {
    // private VARs go here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Create the fragment
        val userCreateFragment = UserCreateFragment()

        //Replace the fragment container
        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fl_frag_container, userCreateFragment, "userCreat_frag")
        fTrans.commit()
    }

    override fun receiveUserProfile(data: UserData?) {
        //send a bundle with user data
        mUser = data
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
    }



    companion object {
        private var mUser: UserData? = null
    }
}