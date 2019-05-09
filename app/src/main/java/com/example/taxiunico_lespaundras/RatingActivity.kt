package com.example.taxiunico_lespaundras

import ApiUtility.ApiClient
import ApiUtility.Taxi
import ApiUtility.TaxiTrip
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_rating.*

class RatingActivity : AppCompatActivity() {

    lateinit var trip: TaxiTrip
    lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        trip = intent.extras.getParcelable(FragmentHome.TRIP)
        email = intent.extras.getString(LoginActivity.EMAIL)

        rating_text_driver_info.text = trip.taxi.driverName

        rating_button_rate.setOnClickListener {
            ApiClient(this@RatingActivity).rateDriver(trip.id, rating_driver_stars.rating) {_, success, message ->
                if(success) {
                    val navIntent = Intent(this@RatingActivity, NavbarActivity::class.java).apply {
                        putExtra(LoginActivity.EMAIL, email)
                    }
                    startActivity(navIntent)
                } else {
                    Toast.makeText(this@RatingActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        rating_driver_stars.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->  }
    }
}
