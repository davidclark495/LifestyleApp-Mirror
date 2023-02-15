package com.thegoodlife

import androidx.appcompat.app.AppCompatActivity
import com.thegoodlife.UserCreateFragment.ReceiveUserInterface
import android.os.Bundle

class MainActivity : AppCompatActivity(), ReceiveUserInterface {
//    private var mStringFirstName: String? = null
//    private var mTvFirstName: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get stuff
//        mTvFirstName = findViewById<View>(R.id.tv_fn_data) as TextView

        //Create the fragment
//        val submitFragment = SubmitFragment()
        val userCreateFragment = UserCreateFragment()

        //Replace the fragment container
        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fl_frag_container, userCreateFragment, "userCreat_frag")
        fTrans.commit()
    }

    override fun receiveUserProfile(data: User?) {
        mUser = data
    }

    companion object {
        private var mUser: User? = null
    }
}