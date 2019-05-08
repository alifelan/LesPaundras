package com.example.taxiunico_lespaundras

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_rating.*

class RatingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        
        rating_driver_stars.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->  }
    }
}
