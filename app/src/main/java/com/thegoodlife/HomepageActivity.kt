package com.thegoodlife

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.os.Build


class HomepageActivity : AppCompatActivity() {

    private var mProfPic:ImageButton? = null
    private var mHikeButton:ImageButton? = null
    private var mWeatherButton:ImageButton? = null
    private var mBmrText:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_homepage)

        mProfPic = findViewById(R.id.profButton)
        mHikeButton = findViewById(R.id.hikeButton)
        mWeatherButton = findViewById(R.id.weatherButton)
        mBmrText = findViewById(R.id.bmrBox)

        //listeners should handle activity switching
        mProfPic!!.setOnClickListener {
            Toast.makeText(this, "go to profile activity", Toast.LENGTH_SHORT).show()
        }
        mHikeButton!!.setOnClickListener {
            Toast.makeText(this, "go to hike activity", Toast.LENGTH_SHORT).show()
        }
        mWeatherButton!!.setOnClickListener {
            Toast.makeText(this, "go to weather activity", Toast.LENGTH_SHORT).show()
        }

        val receivedIntent = intent

        val user: User?
        if(Build.VERSION.SDK_INT >= 33) {
                user = receivedIntent.getParcelableExtra("User", User::class.java)
            } else {
                user = receivedIntent.getParcelableExtra("User")
            }
        val imagePath = user?.profile_pic_file_path
        val thumbnailImage = BitmapFactory.decodeFile(imagePath)
        if (thumbnailImage != null) {
            mProfPic!!.setImageBitmap(thumbnailImage)
        }
        mBmrText!!.text = user?.bmr?.toString()
    }
}