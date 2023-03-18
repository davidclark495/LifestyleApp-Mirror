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
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 */
class UserCreateFragment : Fragment() {

    // the host Activity, must support an interface/callback
    private var mUserReceiver: ReceiveUserInterface? = null

    // misc. data used to populate views
    private var mCountryList: List<String>? = null
    private var mCityList: List<String>? = null

    // misc. views
    private var mNameET: EditText? = null
    private var mAgeNumPicker: NumberPicker? = null
    private var mWeightNumPicker: NumberPicker? = null
    private var mHeightNumPicker: NumberPicker? = null
    private var mSexSpinner: Spinner? = null
    private var mActivityLevelSpinner: Spinner? = null
    private var mCountrySpinner: Spinner? = null
    private var mCitySpinner: Spinner? = null
    private var mProfilePhotoView: ImageView? = null
    private var mSaveButton: Button? = null
    private var mCameraButton: ImageButton? = null

    // misc. data associated w/ views
    private var mSexStr: String? = null
    private var mActivityLevelStr: String? = null
    private var mCountryStr: String? = null
    private var mCityStr: String? = null
    private var mProfilePhotoBitmap: Bitmap? = null
    private var mProfilePhotoFilePath: String? = null

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

        // load user data
        var user : UserData? = null

        if(arguments != null) {
            user = requireArguments().getParcelable("User")
        }
        if(savedInstanceState != null) {
            //Toast.makeText(activity, "++saved instance state found++", Toast.LENGTH_SHORT).show()
            print("saved instance state found")
            user = savedInstanceState.getParcelable("User")
            //Toast.makeText(activity, user.toString(), Toast.LENGTH_SHORT).show()
        }

        // load country / city data
        val src_ctry_cty = resources.openRawResource(R.raw.country_city)
        val jstr_ctry_cty = src_ctry_cty.bufferedReader().use { it.readText() }
        src_ctry_cty.close()
        val jobj_ctry_cty = JSONObject(jstr_ctry_cty)
        val countries = jobj_ctry_cty.keys().asSequence().toList()
        mCountryList = countries.sorted()

        val view = inflater.inflate(R.layout.fragment_user_create, container, false)

        // Get views
        mNameET = view.findViewById(R.id.et_name) as EditText
        mAgeNumPicker = view.findViewById(R.id.number_age) as NumberPicker
        mWeightNumPicker = view.findViewById(R.id.number_weight) as NumberPicker
        mHeightNumPicker = view.findViewById(R.id.number_height) as NumberPicker
        mSexSpinner = view.findViewById(R.id.spinner_sex) as Spinner
        mActivityLevelSpinner = view.findViewById(R.id.spinner_activity_level) as Spinner
        mCountrySpinner = view.findViewById(R.id.spinner_country) as Spinner
        mCitySpinner = view.findViewById(R.id.spinner_city) as Spinner
        mProfilePhotoView = view.findViewById(R.id.profile_image_view) as ImageView
        mSaveButton = view.findViewById(R.id.button_save) as Button
        mCameraButton = view.findViewById(R.id.button_camera) as ImageButton

        if(user != null) {
            mNameET?.setText(user?.name)
            mProfilePhotoFilePath = user?.profile_pic_file_path
            mProfilePhotoBitmap = user?.profile_pic
        }

        // Setup stuff
        // age
        mAgeNumPicker?.minValue = 0
        mAgeNumPicker?.maxValue = 150
        mAgeNumPicker?.value = 18
        mAgeNumPicker?.wrapSelectorWheel = false
        mAgeNumPicker?.setOnLongPressUpdateInterval(100)
        if(user != null) {
            mAgeNumPicker?.value = user?.age as Int
        }

        // weight
        mWeightNumPicker?.minValue = 0
        mWeightNumPicker?.maxValue = 1000
        mWeightNumPicker?.value = 150
        mWeightNumPicker?.wrapSelectorWheel = false
        mWeightNumPicker?.setOnLongPressUpdateInterval(100)
        mWeightNumPicker?.setFormatter { String.format("%d lbs", it) }
        if(user != null) {
            mWeightNumPicker?.value = user?.weight as Int
        }
        try {
            val method = mWeightNumPicker?.javaClass?.getDeclaredMethod("changeValueByOne", Boolean::class.javaPrimitiveType)
            method?.setAccessible(true)
            method?.invoke(mWeightNumPicker, true) //java.lang.reflect.InvocationTargetException
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // height
        mHeightNumPicker?.minValue = 0
        mHeightNumPicker?.maxValue = 200
        mHeightNumPicker?.value = 60
        mHeightNumPicker?.wrapSelectorWheel = false
        mHeightNumPicker?.setOnLongPressUpdateInterval(100)
        mHeightNumPicker?.setFormatter {
            String.format(
                "%d' %d\" ",
                it / 12,
                it % 12
            )
        }
        if(user != null) {
            mHeightNumPicker?.value = user?.height as Int
        }
        try {
            val method = mHeightNumPicker?.javaClass?.getDeclaredMethod("changeValueByOne", Boolean::class.javaPrimitiveType)
            method?.setAccessible(true)
            method?.invoke(mHeightNumPicker, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val countryAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item,
                mCountryList as List<String>
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                mCountrySpinner?.adapter = adapter
            }
        if(user != null)
        {
            val arr = mCountryList
            val idx = arr?.indexOf(user?.country)
            mCountrySpinner?.setSelection(idx ?: 0)
            mCountryStr = user?.country
            makeCitySpinner(jobj_ctry_cty)//sghetti
            val idx2 = mCityList?.indexOf(user?.city)
            mCitySpinner?.setSelection(idx2 ?: 0)
            mCityStr = user?.city
        }
        mCountrySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // add an anonymous listener class to track changes to spinner's value
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                mCountryStr = parent.getItemAtPosition(pos) as String
                makeCitySpinner(jobj_ctry_cty)//sghetti
                if(arguments != null)
                {
                    user = requireArguments().getParcelable("User")
                    val idx2 = mCityList?.indexOf(user?.city)
                    mCitySpinner?.setSelection(idx2 ?: 0)
                    mCityStr = user?.city
                }
                if(savedInstanceState != null)
                {
                    user = savedInstanceState.getParcelable("User")
                    var idx2 = mCityList?.indexOf(user?.city)
                    mCitySpinner?.setSelection(idx2 ?: 0)
                    mCityStr = user?.city
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                //mCountry = null
            }
        }


        /////

        // sex (spinner)
        ArrayAdapter.createFromResource(
            // spinner documentation --> https://developer.android.com/develop/ui/views/components/spinner?hl=en
            // Create an ArrayAdapter using the string array and a default spinner layout
            requireContext(),
            R.array.sex_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mSexSpinner?.adapter = adapter
        }
        if(user != null)
        {
            val arr = resources.getStringArray(R.array.sex_options)
            val idx = arr.indexOf(user?.sex)
            mSexSpinner?.setSelection(idx)
            mSexStr = user?.sex
        }
        mSexSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // add an anonymous listener class to track changes to spinner's value
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                mSexStr = parent.getItemAtPosition(pos) as String
                mUserReceiver?.receiveUserProfile(buildUserFromFields())
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
            mActivityLevelSpinner?.adapter = adapter
        }
        if(user != null)
        {
            val arr = resources.getStringArray(R.array.activity_level_options)
            val idx = arr.indexOf(user?.activity_level)
            mActivityLevelSpinner?.setSelection(idx)
            mActivityLevelStr = user?.activity_level
        }
        mActivityLevelSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                // add an anonymous listener class to track changes to spinner's value
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    mActivityLevelStr = parent.getItemAtPosition(pos) as String
                    mUserReceiver?.receiveUserProfile(buildUserFromFields())
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    //mActivityLevelStr = null
                }
            }
        // save button
        mSaveButton?.setOnClickListener {
            // build user from fields & trigger ReceiveUserInterface Callback
            val user = buildUserFromFields()
            mUserReceiver?.receiveUserProfile(user)

            // replace this fragment w/ a new Homepage fragment
            val homepageFragment = HomepageFragment()
            val args = Bundle()
            args.putParcelable("User", user)
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
                ex.printStackTrace()
            }
        }
        return view
    }

    private fun makeCitySpinner(jobj: JSONObject)
    {
        var cities = listOf("Select a Country")

        if(mCountryStr != null)
        {
            val citiesJ = jobj.getJSONArray(mCountryStr)// as Array<String> //is a json array
            val citiesS = Array(citiesJ.length()) { citiesJ.getString(it) }
            val sort2 = citiesS.toList().sorted()
            cities = sort2
            mCityList = cities
        }

        val cityAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item,
                cities
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                mCitySpinner?.adapter = adapter
            }
        mCitySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // add an anonymous listener class to track changes to spinner's value
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                mCityStr = parent.getItemAtPosition(pos) as String
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
            finalBitmap?.compress(Bitmap.CompressFormat.JPEG, 90, out)
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
                val extras = result.data?.extras
                mProfilePhotoBitmap = extras?.get("data") as Bitmap?

                //Open a file and write to it
                if (isExternalStorageWritable) {
                    val filePathString = saveImage(mProfilePhotoBitmap)
                    this.mProfilePhotoFilePath = filePathString
                } else {
                    //Toast.makeText(this, "External storage not writable.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun buildUserFromFields(): UserData {
        val user = UserData(
            mNameET?.text.toString(),
            mAgeNumPicker?.value,
            mWeightNumPicker?.value,
            mHeightNumPicker?.value,
            mSexStr,
            mActivityLevelStr,
            mProfilePhotoBitmap,
            mProfilePhotoFilePath,
            mCountryStr,
            mCityStr
        )
        return user
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val user = buildUserFromFields()

        outState.putParcelable("User", user)
        super.onSaveInstanceState(outState)

        //Toast.makeText(activity, "saving state instance", Toast.LENGTH_SHORT).show()
    }

}