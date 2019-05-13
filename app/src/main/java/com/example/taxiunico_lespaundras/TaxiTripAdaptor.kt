package com.example.taxiunico_lespaundras

import ApiUtility.TaxiTrip
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row.*

/**
 * This class contains information of the pending trips the user has
 */
class TaxiTripAdaptor(private val context: Context, private val trips: MutableList<TaxiTrip>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?):View {
        val taxiTripHolder: TaxiTripViewHolder
        val rowView: View
        val trip: TaxiTrip = getItem(position) as TaxiTrip

        if(convertView == null) {
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(R.layout.row, parent, false)
            taxiTripHolder = TaxiTripViewHolder(rowView)
            rowView.tag = taxiTripHolder
        } else {
            rowView = convertView
            taxiTripHolder = convertView.tag as TaxiTripViewHolder
        }
        taxiTripHolder.bind(trip)
        return rowView
    }

    override fun getItem(position: Int): Any {
        return trips[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return trips.size
    }

    inner class TaxiTripViewHolder(override val containerView: View) : LayoutContainer {
        fun bind(trip: TaxiTrip) {
            row_text_trip_code.text = "Trip code: ${trip.busTrip.id}"
            row_text_price.text = "$${trip.price}"
            row_text_src_dest.text = "${trip.busTrip.origin.city} - ${trip.busTrip.destination.city}"
            row_text_src_address.text = "Source: " + trip.origin.address + ", " + trip.origin.city
            row_text_dest_address.text = "Destination: " + trip.destination.address + ", " + trip.destination.city
            row_text_date.text = trip.departureDate
            row_text_driver.text = trip.taxi.driverName
            row_text_driver_plate.text = trip.taxi.plate
            row_text_status.text = "Status: ${when(trip.status) {
                "CA" -> "Cancelled"
                "PE" -> "Pending"
                "AC" -> "Active"
                else -> "Completed"
            }}"
        }
    }
}