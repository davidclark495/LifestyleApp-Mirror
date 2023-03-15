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


/**
 *
 */
class UserCreateFragment : Fragment() {

    private var mCountrySpinner: Spinner? = null
    private var mCitySpinner: Spinner? = null
    private var countryList: List<String>? = null
    private var cityList: List<String>? = null
    private var mCountry: String? = null
    private var mCity: String? = null

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
    private var mProfilePhotoFilePath: String? = null

    // views for misc. UI
    private var mSaveButton: Button? = null
    private var mCameraButton: ImageButton? = null
    private var mCalculateBMRText: TextView? = null
    private var mCalculateBMRButton: Button? = null
    private var mBMRVal: Double? = null

    // Callback interface
    interface ReceiveUserInterface {
        fun receiveUserProfile(data: UserData?)
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

        var user : UserData?//= null // as UserData



        if(arguments == null)
        {
            Toast.makeText(activity, "):null instance:(", Toast.LENGTH_SHORT).show()
            print("null instance")

        }
        else
        {
            //Toast.makeText(activity, "(:instance not null:)", Toast.LENGTH_SHORT).show()
            print("instance not null")
            user = requireArguments().getParcelable("User")
            //Toast.makeText(activity, user.toString(), Toast.LENGTH_SHORT).show()

        }

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
/*
        if(arguments != null)
        {
            photo = requireArguments().getParcelable("Photo")
            mNameET!!.setText(user!!.name)
            //makeCitySpinner(jobj)//sghetti
        }
*/
        mSaveButton = view.findViewById(R.id.button_save) as Button
        mCameraButton = view.findViewById(R.id.button_camera) as ImageButton
        //mCalculateBMRButton = view.findViewById(R.id.button_bmr) as Button
        //mCalculateBMRText = view.findViewById(R.id.bmr_text) as TextView

        if(arguments != null)
        {
            user = requireArguments().getParcelable("User")
            mNameET!!.setText(user!!.name)
            mProfilePhotoFilePath = user!!.profile_pic_file_path
            mProfilePhotoBitmap = user!!.profile_pic
            //makeCitySpinner(jobj)//sghetti
        }

        // Setup stuff
        // age
        mAgeNumPicker!!.minValue = 0
        mAgeNumPicker!!.maxValue = 150
        mAgeNumPicker!!.value = 18

        if(arguments != null)
        {
            user = requireArguments().getParcelable("User")
            mAgeNumPicker!!.value = user!!.age as Int
            //makeCitySpinner(jobj)//sghetti
        }

        mAgeNumPicker!!.wrapSelectorWheel = false
        mAgeNumPicker!!.setOnLongPressUpdateInterval(100)
        // weight
        mWeightNumPicker!!.minValue = 0
        mWeightNumPicker!!.maxValue = 1000
        mWeightNumPicker!!.value = 150

        if(arguments != null)
        {
            user = requireArguments().getParcelable("User")
            mWeightNumPicker!!.value = user!!.weight as Int
            //makeCitySpinner(jobj)//sghetti
        }

        mWeightNumPicker!!.wrapSelectorWheel = false
        mWeightNumPicker!!.setFormatter { String.format("%d lbs", it) }
        mWeightNumPicker!!.setOnLongPressUpdateInterval(100)
        try {
            val method = mWeightNumPicker!!.javaClass.getDeclaredMethod("changeValueByOne", Boolean::class.javaPrimitiveType)
            method.setAccessible(true)
            method.invoke(mWeightNumPicker, true) //java.lang.reflect.InvocationTargetException
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
        // height
        mHeightNumPicker!!.minValue = 0
        mHeightNumPicker!!.maxValue = 200
        mHeightNumPicker!!.value = 60 //arguments == null ? arguments.user.height : 60

        if(arguments != null)
        {
            user = requireArguments().getParcelable("User")
            mHeightNumPicker!!.value = user!!.height as Int
            //makeCitySpinner(jobj)//sghetti
        }

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
        if(arguments != null)
        {
            user = requireArguments().getParcelable("User")
            //error : arr is an integer not string[]
            var arr = countryList //arrayOf(countryList) as Array<String> //classcast?
            var idx = arr!!.indexOf(user?.country)
            mCountrySpinner!!.setSelection(idx)
            mCountry = user!!.country
            makeCitySpinner(jobj)//sghetti
            var arr2 = cityList //as Array<String> //classcast?
            var idx2 = arr2!!.indexOf(user?.city)
            mCitySpinner!!.setSelection(idx2)
            mCity = user!!.city
        }
        mCountrySpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // add an anonymous listener class to track changes to spinner's value
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                mCountry = parent.getItemAtPosition(pos) as String
                makeCitySpinner(jobj)//sghetti
                if(arguments != null)
                {
                    user = requireArguments().getParcelable("User")
                    var arr2 = cityList //as Array<String> //classcast?
                    var idx2 = arr2!!.indexOf(user?.city)
                    mCitySpinner!!.setSelection(idx2)
                    mCity = user!!.city
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //mCountry = null

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
        if(arguments != null)
        {
            user = requireArguments().getParcelable("User")
            //error : arr is an integer not string[]
            var arr = resources.getStringArray(R.array.sex_options)// as Array<String> //classcast?
            var idx = arr.indexOf(user?.sex)
            mSexSpinner!!.setSelection(idx)
            mSexStr = user!!.sex
        }
        mSexSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // add an anonymous listener class to track changes to spinner's value
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                mSexStr = parent.getItemAtPosition(pos) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //mSexStr = null

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
        if(arguments != null)
        {
            user = requireArguments().getParcelable("User")
            //error : arr is an integer not string[]
            var arr = resources.getStringArray(R.array.activity_level_options)// as Array<String> //classcast?
            var idx = arr.indexOf(user?.activity_level)
            mActivityLevelSpinner!!.setSelection(idx)
            mActivityLevelStr = user!!.activity_level
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
                    //mActivityLevelStr = null

                }
            }
        // save button
        mSaveButton!!.setOnClickListener {
            // build user from fields & trigger ReceiveUserInterface Callback
            val user = UserData(
                mNameET!!.text.toString(),
                mAgeNumPicker!!.value,
                mWeightNumPicker!!.value,
                mHeightNumPicker!!.value,
                mSexStr,
                mActivityLevelStr,
                mProfilePhotoBitmap,
                mProfilePhotoFilePath,
                mCountry,
                mCity
            )
            mUserReceiver!!.receiveUserProfile(user)

            // replace this fragment w/ a new Homepage fragment
            val homepageFragment = HomepageFragment()
            val args = Bundle()
            args.putParcelable("User", user)
            //new
            //args.putParcelable("ProfilePhotoBitmap", mProfilePhotoBitmap)
            //args.putString("ProfilePicFilePath", user?.profile_pic_file_path)
            homepageFragment.arguments = args
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fl_frag_container, homepageFragment, "homepage_frag")
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        mCameraButton?.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                cameraLauncher.launch(cameraIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        }
/*
        //should have a calculate bmr button
        mCalculateBMRButton!!.setOnClickListener {
            val bmr = calcBMR()
            try {//try display if valid bmr
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
            cityList = cities
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
                //mCity = null
            }
        }
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
                    this.mProfilePhotoFilePath = filePathString
                } else {
                    //Toast.makeText(this, "External storage not writable.", Toast.LENGTH_SHORT).show()
                }    }
        }

    // Misc. Fragment Lifecycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //

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
        //outState.putParcelable("ProfilePhotoBitmap", mProfilePhotoBitmap)
        //outState.putParcelable("User", user)
    }

}