package com.example.taxiunico_lespaundras

import ApiUtility.Address
import ApiUtility.ApiClient
import ApiUtility.BusTrip
import ApiUtility.User
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_trip.*
import kotlinx.android.synthetic.main.fragment_add_trip.*

class AddTripActivity : AppCompatActivity() {

    lateinit var trip: BusTrip
    lateinit var user: User
    var source: Address? = null
    var destination: Address? = null
    var first = false
    var fare = 0.007


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)

        trip = intent.extras.getParcelable(NavbarActivity.TRIP)
        first = intent.extras.getBoolean(NavbarActivity.FIRST)
        user = intent.extras.getParcelable(NavbarActivity.USER)

        if(!first) {
            val aux = trip.origin
            trip.origin = trip.destination
            trip.destination = aux
        }

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

        add_trip_activity_button_ok.setOnClickListener {
            if(source != null) {
                ApiClient(this).getDirections(source?.address!!, "${trip.origin.address},${trip.origin.city},${trip.origin.state}") { route, success, message ->
                    if(success) {
                        val tripT = if(first) 1 else 3
                        ApiClient(this@AddTripActivity).createTaxiTrip(user.email, trip.id, trip.origin.state, trip.origin.city, source?.address!!, source?.coordinates!!, tripT, route?.distance?.value!!*fare, route.distance, route.duration) { _, _, _ ->

                        }
                    }
                }
            }
            if(destination != null) {
                ApiClient(this).getDirections(destination?.address!!, "${trip.destination.address},${trip.destination.city},${trip.destination.state}") { route, success, message ->
                    if(success) {
                        val tripT = if(first) 2 else 4
                        ApiClient(this@AddTripActivity).createTaxiTrip(user.email, trip.id, trip.destination.state, trip.destination.city, destination?.address!!, destination?.coordinates!!, tripT, route?.distance?.value!!*fare, route.distance, route.duration) { _, _, _ ->

                        }
                    }
                }
            }
            if(trip.roundtrip && first) {
                val addTripIntent = Intent(this@AddTripActivity, AddTripActivity::class.java).apply {
                    putExtra(NavbarActivity.TRIP, trip)
                    putExtra(NavbarActivity.FIRST, false)
                    putExtra(NavbarActivity.USER, user)
                }
                startActivity(addTripIntent)
            } else {
                val navIntent = Intent(this, NavbarActivity::class.java).apply {
                    putExtra(LoginActivity.EMAIL, user.email)
                }
                startActivity(navIntent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == TRIP_START) {
                source = data?.extras?.getParcelable(AddAddressActivity.ADDRESS)
                add_trip_activity_button_add_src_address.text = getString(R.string.address_success)
            } else if(requestCode == TRIP_END) {
                destination = data?.extras?.getParcelable(AddAddressActivity.ADDRESS)
                add_trip_activity_button_add_dest_address.text = getString(R.string.address_success)
            }
        }
    }

    companion object {
        const val SRCDEST: String = "srcDest"
        const val CURRENT_TRIP: String ="current"
        const val FIRST: String = "first"
        const val USER: String = "user"
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
