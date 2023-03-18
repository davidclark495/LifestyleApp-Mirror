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

private const val ARG_USER = "User"

/**
 * A simple [Fragment] subclass.
 */
class HeaderFragment : Fragment() {
    private var mUser: UserData? = null
    private var mProfPic: ImageButton? = null
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_header, container, false)

        mProfPic = view.findViewById(R.id.profButton)
        mBmrText = view.findViewById(R.id.tv_bmr)

        // set profile pic, if any
        val imagePath = mUser?.profile_pic_file_path
        val thumbnailImage = BitmapFactory.decodeFile(imagePath)
        if (thumbnailImage != null) {
            mProfPic!!.setImageBitmap(thumbnailImage)
        }

        // set user's bmr
        mBmrText!!.text = mUser?.bmr?.toString()

        // tapping the profile pic allows user to go to profile in main (i.e. lower) fragment
        mProfPic!!.setOnClickListener {
            // make new fragment
            val userFragment = UserCreateFragment()
            val args = Bundle()
            args.putParcelable("User", mUser)
            userFragment.arguments = args
            // switch to fragment
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fl_frag_container, userFragment, "userCreate_frag")
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        return view
    }

}