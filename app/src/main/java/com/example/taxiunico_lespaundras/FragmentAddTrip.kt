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
                        val addTripIntent = Intent(activity, AddTripActivity::class.java).apply {
                            putExtra(NavbarActivity.TRIP, trip)
                            putExtra(NavbarActivity.FIRST, true)
                            putExtra(NavbarActivity.USER, model.user)
                        }
                        startActivity(addTripIntent)
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
}