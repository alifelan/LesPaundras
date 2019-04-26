package com.example.taxiunico_lespaundras

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_address.*

class AddAddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        // back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // obtain intent data and display "source" or "destination" in title
        val data = intent.extras
        val srcDest: String = data.getString(AddTripActivity.SRCDEST)
        add_address_text_title.text = "Add " + srcDest + " address"
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
