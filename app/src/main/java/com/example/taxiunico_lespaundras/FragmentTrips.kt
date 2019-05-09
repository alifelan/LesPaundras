package com.example.taxiunico_lespaundras

import ApiUtility.ApiClient
import ViewModels.UserViewModel
import android.arch.lifecycle.ViewModelProviders
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
class FragmentTrips : Fragment() {
    private lateinit var model: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trips, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid activity")
        ApiClient(activity?.applicationContext!!).getUserTaxiTrips(model.user?.email!!) {trips, success, message ->
            if(success) {
                val pastTrips = trips?.pastTrips!!
                pastTrips.addAll(trips.cancelledTrips)
                val tripsAdapter: TaxiTripAdaptor = TaxiTripAdaptor(activity?.applicationContext!!, pastTrips)
                tripsAdapter.notifyDataSetChanged()
                trips_list_past.adapter = tripsAdapter
                val pastAdapter: TaxiTripPastAdaptor = TaxiTripPastAdaptor(activity?.applicationContext!!, trips.futureTrips)
                pastAdapter.notifyDataSetChanged()
                trips_list_upcoming.adapter = pastAdapter
            } else {
                Toast.makeText(activity?.applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}