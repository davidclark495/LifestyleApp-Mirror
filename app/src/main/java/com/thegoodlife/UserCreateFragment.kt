package com.thegoodlife


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.Button
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import java.lang.ClassCastException
import android.content.Context



/**
 *
 */
class UserCreateFragment : Fragment() {

    private var mNameET: EditText? = null
    private var mAgeNumPicker: NumberPicker? = null
    private var mWeightNumPicker: NumberPicker? = null
    private var mHeightNumPicker: NumberPicker? = null
    private var mSexSpinner: Spinner? = null
    private var mActivityLevelSpinner: Spinner? = null
    private var mSaveButton: Button? = null

    private var mUserReceiver: ReceiveUserInterface? = null


    // Callback interface
    interface ReceiveUserInterface {
        fun receiveUserProfile(data: User?)
    }

    // attach to parent Activity, Activity must implement ReceiveUserInterface
    override fun onAttach(context: Context){
        super.onAttach(context)
        mUserReceiver = try {
            context as ReceiveUserInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement UserCreateFragment.ReceiveUserInterface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_create, container, false)

        // Get stuff
        mNameET = view.findViewById(R.id.et_name) as EditText
        mAgeNumPicker = view.findViewById(R.id.number_age) as NumberPicker
        mWeightNumPicker = view.findViewById(R.id.number_weight) as NumberPicker
        mHeightNumPicker = view.findViewById(R.id.number_height) as NumberPicker
        mSexSpinner = view.findViewById(R.id.spinner_sex) as Spinner
        mActivityLevelSpinner = view.findViewById(R.id.spinner_activity_level) as Spinner
        mSaveButton = view.findViewById(R.id.button_save) as Button

        // Setup stuff
        // age
        mAgeNumPicker!!.minValue = 0
        mAgeNumPicker!!.maxValue = 150
        mAgeNumPicker!!.value = 18
        mAgeNumPicker!!.wrapSelectorWheel = false
        mAgeNumPicker!!.setOnLongPressUpdateInterval(100)
        // weight
        mWeightNumPicker!!.minValue = 0
        mWeightNumPicker!!.maxValue = 1000
        mWeightNumPicker!!.value = 150
        mWeightNumPicker!!.wrapSelectorWheel = false
        mWeightNumPicker!!.setOnLongPressUpdateInterval(100)
        mWeightNumPicker!!.setFormatter(NumberPicker.Formatter { String.format("%d lbs",it) })
        // height
        mHeightNumPicker!!.minValue = 0
        mHeightNumPicker!!.maxValue = 200
        mHeightNumPicker!!.value = 60
        mHeightNumPicker!!.wrapSelectorWheel = false
        mHeightNumPicker!!.setOnLongPressUpdateInterval(100)
        mHeightNumPicker!!.setFormatter(NumberPicker.Formatter { String.format("%d' %d\" ",it/12, it%12) })

        // spinner documentation --> https://developer.android.com/develop/ui/views/components/spinner?hl=en
        // Create an ArrayAdapter using the string array and a default spinner layout
        // sex (spinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sex_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            mSexSpinner!!.adapter = adapter
        }
        // activity level (spinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.activity_level_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            mActivityLevelSpinner!!.adapter = adapter
        }

        // save button
        mSaveButton!!.setOnClickListener{
            // build user from fields & trigger ReceiveUserInterface Callback
            val user = User(
                mNameET!!.text.toString(),
                mAgeNumPicker!!.value,
                mWeightNumPicker!!.value,
                mHeightNumPicker!!.value,
                null,
                null,
                null
            )
            mUserReceiver!!.receiveUserProfile(user)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(
            activity,
            "Toast! for 'UserCreateFragment.kt'",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }
}