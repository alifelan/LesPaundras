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
package ApiUtility

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * User data class to hold results from api
 */
@Parcelize
data class User(
    @SerializedName("name") var name: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("rating") var rating : Double = 0.0,
    @SerializedName("trips") var trips : Int = 0
) : JSONConvertable, Parcelable

/**
 * Place data class to hold results from api
 */
@Parcelize
data class Place(
    @SerializedName("id") var id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("state") var state: String = "",
    @SerializedName("city") var city: String = "",
    @SerializedName("address") var address: String = "",
    @SerializedName("latitude") var latitue: Double = 0.0,
    @SerializedName("longitude") var longitude: Double = 0.0
) : JSONConvertable, Parcelable

/**
 * BusTrip data class to hold results from api
 */
@Parcelize
data class BusTrip(
    @SerializedName("id") var id: String = "",
    @SerializedName("origin") var origin: Place,
    @SerializedName("destination") var destination: Place,
    @SerializedName("first_departure_date") var first_departure_date: String = "",
    @SerializedName("first_arrival_date") var first_arrival_date: String = "",
    @SerializedName("second_departure_date") var second_departure_date: String = "",
    @SerializedName("second_arrival_date") var second_arrival_date: String = "",
    @SerializedName("round_trip") var roundtrip: Boolean
) : JSONConvertable, Parcelable

/**
 * ValueText data class to hold results from api
 */
@Parcelize
data class ValueText(
    @SerializedName("value") var value: Int = 0,
    @SerializedName("text") var text: String = ""
): JSONConvertable, Parcelable

/**
 * Route data class to hold results from api
 */
@Parcelize
data class Route(
    var points: List<LatLng>,
    var duration: ValueText,
    var distance: ValueText
) : Parcelable

/**
 * Address data class to hold results from api
 */
@Parcelize
data class Address(
    var address: String,
    var coordinates: LatLng
) : Parcelable

/**
 * Taxi data class to hold results from api
 */
@Parcelize
data class Taxi(
    @SerializedName("email") var email: String = "",
    @SerializedName("driver_name") var driverName: String = "",
    @SerializedName("plate") var plate: String = "",
    @SerializedName("model") var model: String = "",
    @SerializedName("brand") var brand: String = "",
    @SerializedName("taxi_number") var taxi_number: String = ""
) : JSONConvertable, Parcelable

/**
 * TaxiTrip data class to hold results from api
 */
@Parcelize
data class TaxiTrip(
    @SerializedName("id") var id: String = "",
    @SerializedName("origin") var origin: Place,
    @SerializedName("destination") var destination: Place,
    @SerializedName("bus_trip") var busTrip: BusTrip,
    @SerializedName("departure_date") var departureDate: String = "",
    @SerializedName("arrival_date") var arrivalDate: String = "",
    @SerializedName("user") var user: User,
    @SerializedName("taxi") var taxi: Taxi,
    @SerializedName("price") var price: Double,
    @SerializedName("taxi_rating") var taxiRating: Double,
    @SerializedName("user_rating") var userRating: Double,
    @SerializedName("status") var status: String
) : JSONConvertable, Parcelable

@Parcelize
data class UserTaxiTrips(
    @SerializedName("past_trips") var pastTrips: MutableList<TaxiTrip>,
    @SerializedName("future_trips") var futureTrips: MutableList<TaxiTrip>,
    @SerializedName("current_trip") var currentTrips: MutableList<TaxiTrip>,
    @SerializedName("cancelled_trips") var cancelledTrips: MutableList<TaxiTrip>
) : JSONConvertable, Parcelable

