package com.example.taxiunico_lespaundras

import ApiUtility.TaxiTrip
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row.*

class TaxiTripAdaptor(private val context: Context, private val trips: MutableList<TaxiTripAdaptor>) : BaseAdapter() {

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
            row_text_trip_code.text = trip.busTrip.id
            row_text_price.text = trip.price.toString()
            row_text_src_dest.text = "${trip.origin.name}-${trip.destination.name}"
            row_text_src_address.text = trip.origin.address
            row_text_dest_address.text = trip.destination.address
            row_text_date.text = trip.date
            row_text_driver.text = trip.taxi.driverName
            row_text_driver_plate.text = trip.taxi.plate
            row_text_status.text = trip.status
        }
    }
}