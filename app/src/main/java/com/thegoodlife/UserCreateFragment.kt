package com.thegoodlife


import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.InvocationTargetException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round


/**
 *
 */
class UserCreateFragment : Fragment() {

    private var mHomeIntent: Intent? = null
    private var mCountrySpinner: Spinner? = null
    private var mCitySpinner: Spinner? = null
    private var countryList: List<String>? = null
    private var mCountry: String? = null
    private var mCity: String? = null
    //private var cityList: Array<String>? = null

    // the host Activity, must support an interface/callback
    private var mUserReceiver: ReceiveUserInterface? = null

    // views for "user" data
    private var mNameET: EditText? = null
    private var mAgeNumPicker: NumberPicker? = null
    private var mWeightNumPicker: NumberPicker? = null
    private var mHeightNumPicker: NumberPicker? = null
    private var mSexSpinner: Spinner? = null
    private var mSexStr: String? = null
    private var mActivityLevelSpinner: Spinner? = null
    private var mActivityLevelStr: String? = null
    private var mProfilePhotoView: ImageView? = null
    private var mProfilePhotoBitmap: Bitmap? = null

    // views for misc. UI
    private var mSaveButton: Button? = null
    private var mCameraButton: Button? = null
    private var mCalculateBMRText: TextView? = null
    private var mCalculateBMRButton: Button? = null
    private var mBMRVal: Double? = null

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



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mHomeIntent = Intent(activity, HomepageActivity::class.java)
        var input = resources.openRawResource(R.raw.country_city)
        var jstr = input.bufferedReader().use { it.readText() }
        input.close()

        val jobj = JSONObject(jstr)

        var countries = jobj.keys().asSequence().toList()
        var sort = countries.sorted()
        countryList = sort

        val view = inflater.inflate(R.layout.fragment_user_create, container, false)

        //new views
        mCountrySpinner = view.findViewById(R.id.spinner_country) as Spinner
        mCitySpinner = view.findViewById(R.id.spinner_city) as Spinner

        // Get views
        mNameET = view.findViewById(R.id.et_name) as EditText
        mAgeNumPicker = view.findViewById(R.id.number_age) as NumberPicker
        mWeightNumPicker = view.findViewById(R.id.number_weight) as NumberPicker
        mHeightNumPicker = view.findViewById(R.id.number_height) as NumberPicker
        mSexSpinner = view.findViewById(R.id.spinner_sex) as Spinner
        mActivityLevelSpinner = view.findViewById(R.id.spinner_activity_level) as Spinner
        mProfilePhotoView = view.findViewById(R.id.profile_image_view) as ImageView
        mSaveButton = view.findViewById(R.id.button_save) as Button
        mCameraButton = view.findViewById(R.id.button_camera) as Button
        //mCalculateBMRButton = view.findViewById(R.id.button_bmr) as Button
        //mCalculateBMRText = view.findViewById(R.id.bmr_text) as TextView

        // Setup stuff
        // age
        mAgeNumPicker!!.minValue = 0
        mAgeNumPicker!!.maxValue = 150
        mAgeNumPicker!!.value = 18
        mAgeNumPicker!!.wrapSelectorWheel = false
        mAgeNumPicker!!.setOnLongPressUpdateInterval(100)
        // weightSS
        mWeightNumPicker!!.minValue = 0
        mWeightNumPicker!!.maxValue = 1000
        mWeightNumPicker!!.value = 150
        mWeightNumPicker!!.wrapSelectorWheel = false
        mWeightNumPicker!!.setFormatter { String.format("%d lbs", it) }
        mWeightNumPicker!!.setOnLongPressUpdateInterval(100)
        try {
            val method = mWeightNumPicker!!.javaClass.getDeclaredMethod("changeValueByOne", Boolean::class.javaPrimitiveType)
            method.setAccessible(true)
            method.invoke(mWeightNumPicker, true)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        mWeightNumPicker!!.setOnClickListener {
            ;
        }

        mHeightNumPicker!!.minValue = 0
        mHeightNumPicker!!.maxValue = 200
        mHeightNumPicker!!.value = 60
        mHeightNumPicker!!.wrapSelectorWheel = false
        mHeightNumPicker!!.setOnLongPressUpdateInterval(100)
        mHeightNumPicker!!.setFormatter {
            String.format(
                "%d' %d\" ",
                it / 12,
                it % 12
            )
        }

        try {
            val method = mHeightNumPicker!!.javaClass.getDeclaredMethod("changeValueByOne", Boolean::class.javaPrimitiveType)
            method.setAccessible(true)
            method.invoke(mHeightNumPicker, true)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        mHeightNumPicker!!.setOnClickListener {
            ;
        }

        /////
        val countryAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item,
                countryList as List<String>
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                mCountrySpinner!!.adapter = adapter
            }
        mCountrySpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // add an anonymous listener class to track changes to spinner's value
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                mCountry = parent.getItemAtPosition(pos) as String
                makeCitySpinner(jobj)//sghetti
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                mCountry = null
            }
        }


        /////

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
                mProfilePhotoBitmap,
                /*
                //User.kt : change constructor, etc.
                //mCountry,
                //mCity
                 */
            )
            mUserReceiver!!.receiveUserProfile(user)

            mHomeIntent!!.putExtra("bmr", calcBMR())
            startActivity(mHomeIntent)

        }
        mCameraButton!!.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                cameraLauncher.launch(cameraIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        }
/*
        //should have a
        //calculate bmr button
        mCalculateBMRButton!!.setOnClickListener {

            //val bmr = calcBMR()

            //encapsulated in method
            val kgWeight: Double = mWeightNumPicker!!.value * 0.45359237
            val cmHeight: Double = mHeightNumPicker!!.value * 2.54
            if (mSexStr == "Male") {
                mBMRVal = round(((10 * (kgWeight)) + (6.25 * cmHeight) - (5 * mAgeNumPicker!!.value) + 5))
            }
            else if (mSexStr == "Female") {
                mBMRVal = round(((10 * (kgWeight)) + (6.25 * cmHeight) - (5 * mAgeNumPicker!!.value) - 161))
            }
            else { // other
                mBMRVal = 0.0
            }
            //try display if valid bmr
            try {
                mCalculateBMRText!!.text =
                    "Your daily target calorie intake is: " + mBMRVal.toString()
            } catch (e: Exception) { }
        }
*/
        return view

    }

    private fun makeCitySpinner(jobj: JSONObject)
    {
        var cities = listOf("Select a Country")

        if(mCountry != null)
        {
            var citiesJ = jobj.getJSONArray(mCountry)// as Array<String> //is a json array

            var citiesS = Array(citiesJ.length()) { citiesJ.getString(it) }

            var sort2 = citiesS.toList().sorted()

            cities = sort2
        }

        val cityAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item,
                cities
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                mCitySpinner!!.adapter = adapter
            }
        mCitySpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // add an anonymous listener class to track changes to spinner's value
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                mCity = parent.getItemAtPosition(pos) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                mCity = null
            }
        }
    }

    private fun calcBMR(): String {
        val kgWeight: Double = mWeightNumPicker!!.value * 0.45359237
        val cmHeight: Double = mHeightNumPicker!!.value * 2.54
        if (mSexStr == "Male") {
            mBMRVal = round(((10 * (kgWeight)) + (6.25 * cmHeight) - (5 * mAgeNumPicker!!.value) + 5))
        }
        else if (mSexStr == "Female") {
            mBMRVal = round(((10 * (kgWeight)) + (6.25 * cmHeight) - (5 * mAgeNumPicker!!.value) - 161))
        }
        else { // other
            mBMRVal = 0.0
        }
        //try display if valid bmr

        return mBMRVal.toString()
        /*
        try {
            mCalculateBMRText!!.text =
                "Your daily target calorie intake is: " + mBMRVal.toString()
        } catch (e: Exception) { }
        */

    }

    private fun saveImage(finalBitmap: Bitmap?): String {
        val root = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fname = "Thumbnail_$timeStamp.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            //Toast.makeText(this, "file saved!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath

    }

        private val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }


    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val extras = result.data!!.extras
                mProfilePhotoBitmap = extras!!["data"] as Bitmap?

                //Open a file and write to it
                if (isExternalStorageWritable) {
                    val filePathString = saveImage(mProfilePhotoBitmap)
                    mHomeIntent!!.putExtra("imagePath", filePathString)
                } else {
                    //Toast.makeText(this, "External storage not writable.", Toast.LENGTH_SHORT).show()
                }    }
        }

    // Misc. Fragment Lifecycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //should probably restore this, no lifecycle at the moment
        // restore profile phot from bundle, if possible (WIP)
//        try {
//            if(Build.VERSION.SDK_INT >= 33) {
//                mProfilePhotoBitmap = savedInstanceState!!.getParcelable("ProfilePhotoBitmap", Bitmap::class.java)
//            } else {
//                mProfilePhotoBitmap = savedInstanceState!!.getParcelable<Bitmap>("ProfilePhotoBitmap")
//            }
//            mProfilePhotoView!!.setImageBitmap(mProfilePhotoBitmap)
//        } catch (ex: Exception) {
//            Toast.makeText(requireContext(), "Unable to load Profile Photo", Toast.LENGTH_SHORT).show()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("ProfilePhotoBitmap", mProfilePhotoBitmap)
    }

}