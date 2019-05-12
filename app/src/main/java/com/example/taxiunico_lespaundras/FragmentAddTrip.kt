package com.example.taxiunico_lespaundras

import ApiUtility.ApiClient
import ViewModels.UserViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_add_trip.*
import java.lang.Exception

/**
 * Fragment used to connect navbaractivity with AddTripActivity
 */
class FragmentAddTrip : Fragment() {
    private lateinit var model: UserViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        add_trip_button_ok.setOnClickListener {
            if(!text_code.text.isEmpty()) {
                ApiClient(activity?.applicationContext!!).getBusTrip(text_code.text.toString()) { trip, success, message ->
                    if(success) {
                        ApiClient(activity?.applicationContext!!).getUserBusTrips(trip?.id!!, model.user?.email!!) {trip1, trip2, trip3, trip4, success, message ->
                            if(success) {
                                val addTripIntent = Intent(activity, AddTripActivity::class.java).apply {
                                    putExtra(NavbarActivity.TRIP, trip)
                                    putExtra(NavbarActivity.FIRST, trip?.roundtrip ?: false)
                                    putExtra(NavbarActivity.USER, model.user)
                                    putExtra(TRIP_1, trip1)
                                    putExtra(TRIP_2, trip2)
                                    putExtra(TRIP_3, trip3)
                                    putExtra(TRIP_4, trip4)
                                }
                                startActivity(addTripIntent)
                            } else {
                                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_trip, container, false)
    }

    companion object {
        const val TRIP_1 = "Trip1"
        const val TRIP_2 = "Trip2"
        const val TRIP_3 = "Trip3"
        const val TRIP_4 = "Trip4"
    }
}