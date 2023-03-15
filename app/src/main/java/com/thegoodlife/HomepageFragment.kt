package com.thegoodlife

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

/**
 * A simple [Fragment] subclass.
 */

private const val ARG_USER = "User"
class HomepageFragment : Fragment() {

    private var mUser: UserData? = null

    private var mProfPic: ImageButton? = null
    private var mHikeButton: ImageButton? = null
    private var mWeatherButton: ImageButton? = null
    private var mBmrText: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if(Build.VERSION.SDK_INT >= 33) {
                mUser = it.getParcelable(ARG_USER, UserData::class.java)
            } else {
                mUser = it.getParcelable(ARG_USER)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_homepage, container, false)

        mProfPic = view.findViewById(R.id.profButton)
        mHikeButton = view.findViewById(R.id.hikeButton)
        mWeatherButton = view.findViewById(R.id.weatherButton)
        mBmrText = view.findViewById(R.id.bmrBox)

        val imagePath = mUser?.profile_pic_file_path
        val thumbnailImage = BitmapFactory.decodeFile(imagePath)
        if (thumbnailImage != null) {
            mProfPic!!.setImageBitmap(thumbnailImage)
        }

        //listeners should handle fragment switching
        mProfPic!!.setOnClickListener {
//            Toast.makeText(activity, "go to profile fragment", Toast.LENGTH_SHORT).show()
            // make new fragment
            val userFragment = UserCreateFragment()
            val args = Bundle()
            args.putParcelable("User", mUser)
            //args.putString("ProfilePicFilePath", mUser?.profile_pic_file_path)

            //picture and spinners  //args.putParcelable("")
            userFragment.arguments = args
            // switch to fragment
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fl_frag_container, userFragment, "userCreate_frag")
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        mHikeButton!!.setOnClickListener {
            Toast.makeText(activity, "go to hike fragment", Toast.LENGTH_SHORT).show()
            val hikeFragment = HikeFragment()
            val args = Bundle()
            args.putParcelable("User", mUser)
            hikeFragment.arguments = args
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fl_frag_container, hikeFragment, "hike_frag")
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        mWeatherButton!!.setOnClickListener {
            Toast.makeText(activity, "go to weather fragment", Toast.LENGTH_SHORT).show()
            val weatherFragment = WeatherFragment()
            val args = Bundle()
            args.putParcelable("User", mUser)
            weatherFragment.arguments = args
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fl_frag_container, weatherFragment, "weather_frag")
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
/*
        val imagePath = mUser?.profile_pic_file_path
        val thumbnailImage = BitmapFactory.decodeFile(imagePath)
        if (thumbnailImage != null) {
            mProfPic!!.setImageBitmap(thumbnailImage)
        }
 */
        mBmrText!!.text = mUser?.bmr?.toString()
        return view
    }

    // Misc. Fragment Lifecycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //save additional stuff here
        outState.putParcelable("User", mUser)

    }

}