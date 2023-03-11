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
class HomepageFragment : Fragment() {

    private var mProfPic: ImageButton? = null
    private var mHikeButton: ImageButton? = null
    private var mWeatherButton: ImageButton? = null
    private var mBmrText: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        //listeners should handle activity switching
        mProfPic!!.setOnClickListener {
            Toast.makeText(activity, "go to profile activity", Toast.LENGTH_SHORT).show()
        }
        mHikeButton!!.setOnClickListener {
            Toast.makeText(activity, "go to hike activity", Toast.LENGTH_SHORT).show()
        }
        mWeatherButton!!.setOnClickListener {
            Toast.makeText(activity, "go to weather activity", Toast.LENGTH_SHORT).show()
        }

        val user: User?
        if(Build.VERSION.SDK_INT >= 33) {
            user = arguments?.getParcelable("User", User::class.java)
        } else {
            user = arguments?.getParcelable("User")
        }
        val imagePath = user?.profile_pic_file_path
        val thumbnailImage = BitmapFactory.decodeFile(imagePath)
        if (thumbnailImage != null) {
            mProfPic!!.setImageBitmap(thumbnailImage)
        }
        mBmrText!!.text = user?.bmr?.toString()
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
    }

}