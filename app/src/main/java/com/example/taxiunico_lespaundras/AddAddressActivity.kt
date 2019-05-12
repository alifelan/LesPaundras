/*
 * MIT License
 *
 * Copyright (c) 2019 José Luis Felán Villaseñor
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

class AddAddressActivity : AppCompatActivity() {

    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap: GoogleMap
    lateinit var coordinates: LatLng
    var found = false

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
        })

        add_address_button_map.setOnClickListener {
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
        }

        add_address_button_ok.setOnClickListener {
            if(found) {
                val address: Address = Address(add_address_editable.text.toString(), coordinates)
                val resultIntent: Intent = Intent().apply {
                    putExtra(ADDRESS, address)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
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

    companion object {
        const val ADDRESS = "Address"
    }
}
