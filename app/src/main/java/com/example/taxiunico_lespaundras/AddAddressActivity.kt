package com.example.taxiunico_lespaundras

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_add_address.*

class AddAddressActivity : AppCompatActivity() {

    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        // back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // obtain intent data and display "source" or "destination" in title
        val data = intent.extras
        val srcDest: String = data.getString(AddTripActivity.SRCDEST)
        add_address_text_title.text = "Add " + srcDest + " address"

        mapFragment = supportFragmentManager.findFragmentById(R.id.add_address_map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
            //googleMap.isMyLocationEnabled = true
            val location1 = LatLng(13.03,77.60)
            googleMap.addMarker(MarkerOptions().position(location1).title("My Location"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location1,5f))

            val location2 = LatLng(9.89,78.11)
            googleMap.addMarker(MarkerOptions().position(location2).title("Madurai"))


            val location3 = LatLng(13.00,77.00)
            googleMap.addMarker(MarkerOptions().position(location3).title("Bangalore"))

        })
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
