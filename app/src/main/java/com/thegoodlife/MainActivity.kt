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

        //Get stuff from

        //Create the fragment
        val userCreateFragment = UserCreateFragment()

        //Replace the fragment container
        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fl_main_frag_container, userCreateFragment, "userCreat_frag")
        fTrans.commit()
    }

    override fun receiveUserProfile(data: User?) {
        mUser = data
        // SANITY CHECK: try outputting a sample of User data
        try {
            Toast.makeText(
                this,
                "Received User data: %s, %d, %s, %s".format(
                        mUser!!.name,
                        mUser!!.age,
                        mUser!!.sex,
                        if(mUser!!.profile_pic != null) "(has pic)" else "(no pic)"
            ),
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {}
    }

    companion object {
        private var mUser: User? = null
    }
}