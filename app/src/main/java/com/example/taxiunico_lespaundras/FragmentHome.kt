package com.example.taxiunico_lespaundras

import ApiUtility.ApiClient
import ViewModels.UserViewModel
import android.arch.lifecycle.ViewModelProviders
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
            ApiClient(activity?.applicationContext!!).getCurrentOrNextTrip(model.user?.email!!) {trip, current, success, message ->
                if(success) {
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
                    Toast.makeText(activity?.applicationContext, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}