package com.thegoodlife


import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.lang.ClassCastException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import kotlin.math.round


/**
 *
 */
class UserCreateFragment : Fragment() {

    private var mNameET: EditText? = null
    private var mAgeNumPicker: NumberPicker? = null
    private var mWeightNumPicker: NumberPicker? = null
    private var mHeightNumPicker: NumberPicker? = null
    private var mSexSpinner: Spinner? = null
    private var mSexStr: String? = null
    private var mActivityLevelSpinner: Spinner? = null
    private var mActivityLevelStr: String? = null
    private var mSaveButton: Button? = null
    private var cameraButton: Button? = null
    private var mUserReceiver: ReceiveUserInterface? = null
    private var profilePhoto: ImageView? = null
    private var calculateBMRText: TextView? = null
    private var calculateBMR: Button? = null
    private var bmrVal: Double? = null

    // Callback interface
    interface ReceiveUserInterface {
        fun receiveUserProfile(data: User?)
    }

    // attach to parent Activity, Activity must implement ReceiveUserInterface
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mUserReceiver = try {
            context as ReceiveUserInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement UserCreateFragment.ReceiveUserInterface")
        }
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                profilePhoto = view?.findViewById(R.id.profile_image_view) as ImageView
                val thumbnailImage = result.data!!.getParcelableExtra("data", Bitmap::class.java)
                profilePhoto!!.setImageBitmap(thumbnailImage)

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
        cameraButton = view.findViewById(R.id.button_camera) as Button
        calculateBMR = view.findViewById(R.id.bmr) as Button
        calculateBMRText = view.findViewById(R.id.bmr_text) as TextView
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
        mWeightNumPicker!!.setFormatter(NumberPicker.Formatter { String.format("%d lbs", it) })
        // height
        mHeightNumPicker!!.minValue = 0
        mHeightNumPicker!!.maxValue = 200
        mHeightNumPicker!!.value = 60
        mHeightNumPicker!!.wrapSelectorWheel = false
        mHeightNumPicker!!.setOnLongPressUpdateInterval(100)
        mHeightNumPicker!!.setFormatter(NumberPicker.Formatter {
            String.format(
                "%d' %d\" ",
                it / 12,
                it % 12
            )
        })

        // spinner documentation --> https://developer.android.com/develop/ui/views/components/spinner?hl=en
        // Create an ArrayAdapter using the string array and a default spinner layout
        // sex (spinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sex_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mSexSpinner!!.adapter = adapter
        }
        mSexSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // add an anonymous listener class to track changes to spinner's value
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                mSexStr = parent.getItemAtPosition(pos) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                mSexStr = null
            }
        }
        // activity level (spinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.activity_level_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mActivityLevelSpinner!!.adapter = adapter
        }
        mActivityLevelSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                // add an anonymous listener class to track changes to spinner's value
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    mActivityLevelStr = parent.getItemAtPosition(pos) as String
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    mActivityLevelStr = null
                }
            }

        // save button
        mSaveButton!!.setOnClickListener {
            // build user from fields & trigger ReceiveUserInterface Callback
            val user = User(
                mNameET!!.text.toString(),
                mAgeNumPicker!!.value,
                mWeightNumPicker!!.value,
                mHeightNumPicker!!.value,
                mSexStr,
                mActivityLevelStr,
                null
            )
            mUserReceiver!!.receiveUserProfile(user)
        }
        cameraButton!!.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try {
                cameraLauncher.launch(cameraIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        }

        //calculate bmr button
        calculateBMR!!.setOnClickListener {
            val kgWeight: Double = mWeightNumPicker!!.value * 0.45359237;
            val cmHeight: Double = mHeightNumPicker!!.value * 2.54;
            //male
            if (mSexStr == "Male") {
                bmrVal =
                    round(((10 * (kgWeight)) + (6.25 * cmHeight) - (5 * mAgeNumPicker!!.value) + 5))
            }
            //female
            else if (mSexStr == "Female") {
                bmrVal =
                    round(((10 * (kgWeight)) + (6.25 * cmHeight) - (5 * mAgeNumPicker!!.value) - 161))
            }
            //other
            else {
                bmrVal = 0.0
            }
            //try display if valid bmr
            try {
                calculateBMRText!!.text =
                    "Your daily target calorie intake is: " + bmrVal.toString()
            } catch (e: Exception) {

            }

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