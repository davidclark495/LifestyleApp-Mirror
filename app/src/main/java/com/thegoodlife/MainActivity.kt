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