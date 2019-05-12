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
 */package com.example.taxiunico_lespaundras

import ApiUtility.ApiClient
import ApiUtility.TaxiTrip
import ViewModels.UserViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception

/**
 * Used to show current trip
 */
class FragmentHome : Fragment() {

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    private lateinit var model: UserViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid activity")
        mapFragment = childFragmentManager.findFragmentById(R.id.home_view_map) as SupportMapFragment
        mapFragment.getMapAsync{
            googleMap = it
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(19.4362, -99.1373), 10f))
            setRoute()
        }
    }

    private fun setInfo(trip: TaxiTrip, current: Boolean) {
        if(!current) {
            home_text_title.text = "Next trip"
        }
        home_text_src.text = trip.origin.name
        home_text_dest.text = trip.destination.name
        home_text_driver_info_name.text = trip.taxi.driverName
        home_text_driver_info_brand.text = trip.taxi.brand
        home_text_driver_info_model.text = trip.taxi.model
        home_text_driver_info_plates.text = trip.taxi.plate
        home_text_driver_info_taxiNum.text = trip.taxi.taxi_number
    }

    private fun setInfoVisibility(visibility: Int) {
        home_text_src.visibility = visibility
        home_text_dest.visibility = visibility
        home_text_yourDriver.visibility = visibility
        home_text_driver_info_name.visibility = visibility
        home_text_driver_info_brand.visibility = visibility
        home_text_driver_info_model.visibility = visibility
        home_text_driver_info_plates.visibility = visibility
        home_text_driver_info_taxiNum.visibility = visibility
        home_image_driver.visibility = visibility
    }

    private fun setRoute() {
        ApiClient(activity?.applicationContext!!).getCurrentOrNextTrip(model.user?.email!!) {trip, rate, current, success, message ->
            if(rate != null) {
                val rateIntent = Intent(activity, RatingActivity::class.java).apply {
                    putExtra(TRIP, rate)
                    putExtra(LoginActivity.EMAIL, trip?.user?.email)
                }
                startActivity(rateIntent)
            }
            if(success && trip != null) {
                setInfoVisibility(View.VISIBLE)
                setInfo(trip, current)
                val origin = "${trip?.origin?.address},${trip?.origin?.city},${trip?.origin?.state}"
                val destination = "${trip?.destination?.address},${trip?.destination?.city},${trip?.destination?.state}"
                ApiClient(activity?.applicationContext!!).getDirections(origin, destination){route, success, message ->
                    if(success) {
                        val lineOptions = PolylineOptions()
                        lineOptions.addAll(route?.points)
                        lineOptions.width(6f)
                        lineOptions.color(ContextCompat.getColor(activity?.applicationContext!!, R.color.gray))
                        googleMap.addPolyline(lineOptions)
                        val oLatLng = LatLng(trip?.origin?.latitue!!, trip.origin.longitude)
                        googleMap.addMarker(MarkerOptions().position(oLatLng).title(trip.origin.name))
                        val dLatLng = LatLng(trip.destination.latitue, trip.destination.longitude)
                        googleMap.addMarker(MarkerOptions().position(dLatLng).title(trip.destination.name))
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(oLatLng, 12f))
                    } else {
                        Toast.makeText(activity?.applicationContext!!, message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                home_text_title.text = "No trip"
                setInfoVisibility(View.INVISIBLE)
                Toast.makeText(activity?.applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        const val TRIP = "Trip"
    }
}
