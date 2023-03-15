package com.thegoodlife

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.net.Uri


private const val ARG_USER = "User"

/**
 * A simple [Fragment] subclass.
 */
class HikeFragment : Fragment() {
    private var mUser: UserData? = null
    private var mLocationET: EditText? = null
    private var mSearchBtn: ImageButton? = null

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

        mLocationET = view.findViewById(R.id.et_hikes_near_blank) as EditText
        mSearchBtn = view.findViewById(R.id.button_search_for_hikes) as ImageButton
        mSearchBtn?.setOnClickListener{
            Toast.makeText(requireContext(), "Search Pressed", Toast.LENGTH_SHORT).show()
            val location = mLocationET?.text.toString()
            val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(location + " hikes"))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        return view
    }
}