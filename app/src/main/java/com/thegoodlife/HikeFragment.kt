package com.thegoodlife

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider


/**
 * A simple [Fragment] subclass.
 */
class HikeFragment : Fragment() {
    private lateinit var mUserViewModel: UserViewModel

    private var mLocationET: EditText? = null
    private var mSearchBtn: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hike, container, false)

        mLocationET = view.findViewById(R.id.et_hikes_near_blank) as EditText
        mSearchBtn = view.findViewById(R.id.button_search_for_hikes) as ImageButton
        mSearchBtn?.setOnClickListener{
            val location = mLocationET?.text.toString()
            val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(location + " hikes"))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        mUserViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        // autofill Location w/ user data's location (or w/ blank data if lacking city/country)
        val user = mUserViewModel.currUser.value
        val userHasCityAndCountry = !(user?.city.isNullOrBlank()) && !(user?.country.isNullOrBlank())
        if (user != null && userHasCityAndCountry)
            mLocationET?.setText("%s, %s".format(user?.city,  user?.country))

        return view
    }
}