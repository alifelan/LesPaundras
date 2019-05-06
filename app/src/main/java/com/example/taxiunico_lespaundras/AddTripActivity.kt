package com.example.taxiunico_lespaundras

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_trip.*

class AddTripActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)

        // back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        add_trip_activity_button_add_src_address.setOnClickListener {
            val addSrcIntent = Intent(this, AddAddressActivity::class.java)
            addSrcIntent.putExtra(SRCDEST, "source")
            startActivity(addSrcIntent)
        }

        add_trip_activity_button_add_dest_address.setOnClickListener {
            val addDestIntent = Intent(this, AddAddressActivity::class.java)
            addDestIntent.putExtra(SRCDEST, "destination")
            startActivity(addDestIntent)
        }
    }

    companion object {
        const val SRCDEST: String = "srcDest"
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
