package com.example.taxiunico_lespaundras

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_add_trip.*

class FragmentAddTrip : Fragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        add_trip_button_ok.setOnClickListener {
            if(!text_code.text.isEmpty()) {
                val addTripIntent = Intent(activity, AddTripActivity::class.java).apply {
                    putExtra(NavbarActivity.CODE, text_code.text.toString())
                }
                startActivity(addTripIntent)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_trip, container, false)
    }
}