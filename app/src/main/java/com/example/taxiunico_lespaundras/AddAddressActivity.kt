package com.example.taxiunico_lespaundras

import ApiUtility.Address
import ApiUtility.ApiClient
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_add_address.*

/**
 * Activity used to register an address in a taxi trip creation
 */
class AddAddressActivity : AppCompatActivity() {

    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap: GoogleMap
    var coordinates: LatLng? = null
    var found = false
    var address: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        // back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // obtain intent data and display "source" or "destination" in title
        val data = intent.extras
        val srcDest: String = data.getString(AddTripActivity.SRCDEST)
        address = data.getParcelable(AddTripActivity.ADDRESS)
        if(address != null) {
            add_address_editable.setText(address?.address)
            setMarker()
        }
        add_address_text_title.text = "Add " + srcDest + " address"

        mapFragment = supportFragmentManager.findFragmentById(R.id.add_address_map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it

        })

        add_address_button_map.setOnClickListener {
            setMarker()
        }

        add_address_button_ok.setOnClickListener {
            if(add_address_editable.text.isEmpty() || coordinates == null) {
                Toast.makeText(this, "Please set an address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(found) {
                val address: Address = Address(add_address_editable.text.toString(), coordinates!!)
                val resultIntent: Intent = Intent().apply {
                    putExtra(ADDRESS, address)
                }
                Toast.makeText(this, R.string.address_set, Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    fun setMarker() {
        if(add_address_editable.text.isNotEmpty()) {
            ApiClient(this@AddAddressActivity).getCoordinates(add_address_editable.text.toString()) { coord, success, message ->
                found = false
                if(success && coord != null) {
                    googleMap.addMarker(MarkerOptions().position(coord!!).title(add_address_editable.text.toString()))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, 19f))
                    coordinates = coord
                    found = true
                } else {
                    Toast.makeText(this@AddAddressActivity, "Failed to retrieve location", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Please fill an address", Toast.LENGTH_SHORT).show()
        }
    }

    // back button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(ADDRESS, address)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        address = savedInstanceState?.getParcelable(ADDRESS)
        if(address != null)
            add_address_editable.setText(address?.address)
    }

    companion object {
        const val ADDRESS = "Address"
    }
}
