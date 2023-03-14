package com.thegoodlife

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

private const val ARG_USER = "User"

/**
 * A simple [Fragment] subclass.
 */
class HikeFragment : Fragment() {
    private var mUser: UserData? = null
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
        val view = inflater.inflate(R.layout.fragment_hike, container, false)
        return view
    }
}