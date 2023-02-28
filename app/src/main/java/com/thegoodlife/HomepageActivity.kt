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

        val imagePath = receivedIntent.getStringExtra("imagePath")

        val thumbnailImage = BitmapFactory.decodeFile(imagePath)
        if (thumbnailImage != null) {
            mProfPic!!.setImageBitmap(thumbnailImage)
        }
        val bmr = receivedIntent.getStringExtra("bmr")
        mBmrText!!.text = bmr
    }
}