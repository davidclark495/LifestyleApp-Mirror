package com.thegoodlife

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

/**
 * A simple [Fragment] subclass.
 */

private const val ARG_USER = "User"
class HomepageFragment : Fragment() {

    private var mUser: UserData? = null

    private var mHikeButton: ImageButton? = null
    private var mWeatherButton: ImageButton? = null
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

        mHikeButton = view.findViewById(R.id.hikeButton)
        mWeatherButton = view.findViewById(R.id.weatherButton)

        //listeners should handle fragment switching

        mHikeButton!!.setOnClickListener {
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
            val weatherFragment = WeatherFragment()
            val args = Bundle()
            args.putParcelable("User", mUser)
            weatherFragment.arguments = args
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fl_frag_container, weatherFragment, "weather_frag")
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        return view
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("User", mUser)
    }

}