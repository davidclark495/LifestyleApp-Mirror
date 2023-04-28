package com.thegoodlife

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class LoginScreenFragment : Fragment(){

    private lateinit var mUserViewModel: UserViewModel

    private var mUsernameET: EditText? = null
    private var mSignupButton: Button? = null
    private var mSigninButton: Button? = null

    private var mUserString: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        mSignupButton = view.findViewById(R.id.buttonSignup) as Button
        mSigninButton = view.findViewById(R.id.buttonSignin) as Button
        mUsernameET = view.findViewById(R.id.editTextUsername) as EditText


        //mUserViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)



        mSignupButton?.setOnClickListener {
            // build user from fields & update ViewModel
            //val user = buildUserFromFields()
            //mUserViewModel.updateCurrUser(user)
            //Toast.makeText(requireActivity(), "Welcome, %s".format(user.name), Toast.LENGTH_SHORT).show()

            // replace this fragment w/ a new Homepage fragment
            val userCreateFragment = UserCreateFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()

            transaction?.replace(R.id.fl_login_container, userCreateFragment, "user_frag")
            transaction?.addToBackStack(null)
            transaction?.commit()

            //println("IN USER CREATE FRAGMENT")
            //mUserViewModel.updateCurrUser(buildUserFromFields())

            //send bundle
        }

        mSigninButton?.setOnClickListener {

            var username = mUsernameET?.text.toString()

            var userstring: String? = null

            var wasfound: Boolean = true

            println("attempting...")


            println(username)
            println(userstring)

            println("...attempted")

            userstring = mUserViewModel.getUserData(username)

            while(userstring == null)
            {
                userstring = mUserViewModel.getUserData(username)
                println(userstring)
            }

            println("done")
            println(userstring)

            /*
            catch(e: Exception)
            {
                wasfound = false
                Toast.makeText(requireActivity(), "Username Not Found", Toast.LENGTH_SHORT).show()
                println("<<" + e.localizedMessage + ">>") //null
            }

             */

            if(userstring != null) {

                //println("**" + userstring + "**")

                mUserViewModel.updateCurrUser(buildUserFromString(username, userstring))

                val homepageFragment = HomepageFragment()

                var bundle = Bundle()
                bundle.putString("userstring", userstring)
                homepageFragment.arguments = bundle

                val transaction = activity?.supportFragmentManager?.beginTransaction()

                //need to get the userstring into a userdata object

                transaction?.replace(R.id.fl_login_container, homepageFragment, "homepage_frag")
                transaction?.addToBackStack(null)
                transaction?.commit()
            }

            else
            {
                //toast
            }
        }

        return view
    }


    private fun buildUserFromString(username: String, userstring: String): UserData {

        print(userstring)
        var strings = userstring.split('|')

        val user = UserData(
            //username picker -> saved in bundle from login screen or bundle

            //need to make login screen as the very first thing, then

            username,
            strings[0],
            strings[1] as Int,
            strings[2] as Int,
            strings[3] as Int,
            strings[4],
            strings[5],
            null,//strings[6], //bitmap / load profile picture -> worth it?
            strings[7],//profile-pic-path
            strings[8],
            strings[9],

        )

        return user
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}