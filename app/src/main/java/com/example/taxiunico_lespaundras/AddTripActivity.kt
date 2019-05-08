package com.example.taxiunico_lespaundras

import ApiUtility.Address
import ApiUtility.ApiClient
import ApiUtility.BusTrip
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_trip.*

class AddTripActivity : AppCompatActivity() {

    lateinit var trip: BusTrip
    var source: Address? = null
    var destination: Address? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)

        trip = intent.extras.getParcelable(NavbarActivity.TRIP)

        add_trip_activity_text_src_city.text = trip.origin.name
        add_trip_activity_text_dest_city.text = trip.destination.name

        // back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        add_trip_activity_button_add_src_address.setOnClickListener {
            val addSrcIntent = Intent(this, AddAddressActivity::class.java)
            addSrcIntent.putExtra(SRCDEST, "source")
            startActivityForResult(addSrcIntent, TRIP_START)
        }

        add_trip_activity_button_add_dest_address.setOnClickListener {
            val addDestIntent = Intent(this, AddAddressActivity::class.java)
            addDestIntent.putExtra(SRCDEST, "destination")
            startActivityForResult(addDestIntent, TRIP_END)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == TRIP_START) {
                source = data?.extras?.getParcelable(AddAddressActivity.ADDRESS)
            } else if(requestCode == TRIP_END) {
                destination = data?.extras?.getParcelable(AddAddressActivity.ADDRESS)
            }
        }
    }

    companion object {
        const val SRCDEST: String = "srcDest"
        const val CURRENT_TRIP: String ="current"
        const val TRIP_START: Int = 1
        const val TRIP_END: Int = 2
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(CURRENT_TRIP, trip)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        trip = savedInstanceState?.getParcelable(CURRENT_TRIP)!!
        add_trip_activity_text_src_city.text = trip.origin.name
        add_trip_activity_text_dest_city.text = trip.destination.name
    }

    // back button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
