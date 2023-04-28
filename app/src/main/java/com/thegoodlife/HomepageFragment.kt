package com.thegoodlife

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

/**
 * A simple [Fragment] subclass.
 */

private const val ARG_USER = "User"
class HomepageFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel

    private var mProfPic: ImageButton? = null
    private var mBmrText: TextView? = null

    private var mHikeButton: ImageButton? = null
    private var mWeatherButton: ImageButton? = null

    private var mUserText: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_homepage, container, false)

        mHikeButton = view.findViewById(R.id.hikeButton)
        mWeatherButton = view.findViewById(R.id.weatherButton)

        mProfPic = view.findViewById(R.id.profButton)
        mBmrText = view.findViewById(R.id.tv_bmr)

        if(mProfPic == null)
        {
            mProfPic = view.findViewById(R.id.profButton)
        }

        if(mBmrText == null)
        {
            mBmrText = view.findViewById(R.id.tv_bmr)

        }

        mUserText = view.findViewById(R.id.usernametext)

        mUserViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        mUserViewModel.currUser.observe(requireActivity(), mCurrUserObserver)

        //listeners should handle fragment switching

        mHikeButton!!.setOnClickListener {
            val hikeFragment = HikeFragment()
//            val args = Bundle()
//            hikeFragment.arguments = args

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            val oldfrag = activity?.supportFragmentManager?.findFragmentByTag("homepage_frag")
            transaction?.replace(oldfrag?.getId()!!, hikeFragment, "hike_frag")
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        mWeatherButton!!.setOnClickListener {
            val weatherFragment = WeatherFragment()
//            val args = Bundle()
//            weatherFragment.arguments = args
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            val oldfrag = activity?.supportFragmentManager?.findFragmentByTag("homepage_frag")
            transaction?.replace(oldfrag?.getId()!!, weatherFragment, "weather_frag")
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        mProfPic!!.setOnClickListener {
            // make new fragment
            val userFragment = UserCreateFragment()
//            val args = Bundle()
//            userFragment.arguments = args
            // switch to fragment
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            val oldfrag = activity?.supportFragmentManager?.findFragmentByTag("homepage_frag")
            transaction?.replace(oldfrag?.getId()!!, userFragment, "userCreate_frag")
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        return view
    }



    private val mCurrUserObserver: Observer<UserData?> =
        Observer { user -> // update the UI if this changes
            if (user != null) {
                // set profile pic, if any
                val imagePath = user.profile_pic_file_path
                val thumbnailImage = BitmapFactory.decodeFile(imagePath)
                if (thumbnailImage != null) {
                    mProfPic!!.setImageBitmap(thumbnailImage)
                }

                // set user's bmr

                if(mBmrText != null) {
                    mBmrText!!.text = user.bmr?.toString()
                }
                if(mUserText != null) {
                    mUserText!!.text = user.username
                }
            }
        }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

}