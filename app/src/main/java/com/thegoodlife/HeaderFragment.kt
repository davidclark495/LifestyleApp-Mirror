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
class HeaderFragment : Fragment() {
    private lateinit var mUserViewModel: UserViewModel

    private var mProfPic: ImageButton? = null
    private var mBmrText: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_header, container, false)

        mProfPic = view.findViewById(R.id.profButton)
        mBmrText = view.findViewById(R.id.tv_bmr)

        mUserViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        mUserViewModel.currUser.observe(requireActivity(), mCurrUserObserver)

        // tapping the profile pic allows user to go to profile in main (i.e. lower) fragment
        mProfPic!!.setOnClickListener {
            // make new fragment
            val userFragment = UserCreateFragment()
//            val args = Bundle()
//            userFragment.arguments = args
            // switch to fragment
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fl_frag_container, userFragment, "userCreate_frag")
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
                mBmrText!!.text = user.bmr?.toString()

            }
        }


}