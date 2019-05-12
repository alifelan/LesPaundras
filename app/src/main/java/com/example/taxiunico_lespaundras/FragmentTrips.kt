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
import ApiUtility.Place
import ApiUtility.TaxiTrip
import ViewModels.UserViewModel
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_trips.*
import java.lang.Exception

/**
 * Shows trips pending and completed by a user
 */
class FragmentTrips : Fragment(), UpdateClickListener {
    private lateinit var model: UserViewModel
    lateinit var updateTrip: TaxiTrip

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trips, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid activity")
        fillLists()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == ADDRESS_UPDATE) {
                val address: Address? = data?.extras?.getParcelable(AddAddressActivity.ADDRESS)
                val place: Place = if(updateTrip.busTrip.origin.address == updateTrip.destination.address) updateTrip.origin else updateTrip.destination
                ApiClient(activity?.applicationContext!!).updateTaxiTripAddress(updateTrip.id, address?.address!!, place.state, place.city, address.address, address.coordinates) { trip, success, message ->
                    if(success) {
                        fillLists()
                    } else {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun fillLists() {
        ApiClient(activity?.applicationContext!!).getUserTaxiTrips(model.user?.email!!) {trips, success, message ->
            if(success) {
                val pastTrips = trips?.pastTrips!!
                pastTrips.addAll(trips.cancelledTrips)
                val tripsAdapter: TaxiTripAdaptor = TaxiTripAdaptor(activity?.applicationContext!!, pastTrips)
                tripsAdapter.notifyDataSetChanged()
                trips_list_past.adapter = tripsAdapter
                val pastAdapter: TaxiTripPastAdaptor = TaxiTripPastAdaptor(activity?.applicationContext!!, trips.futureTrips, this)
                pastAdapter.notifyDataSetChanged()
                trips_list_upcoming.adapter = pastAdapter
            } else {
                Toast.makeText(activity?.applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onUpdateClickListener(trip: TaxiTrip) {
        updateTrip = trip
        val updateAddressIntent = Intent(activity, AddAddressActivity::class.java).apply {
            putExtra(AddTripActivity.SRCDEST, "update")
        }
        startActivityForResult(updateAddressIntent, ADDRESS_UPDATE)
    }

    companion object {
        const val ADDRESS_UPDATE = 10
    }
}