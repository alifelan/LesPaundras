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
    @SerializedName("rating") var rating : Int = 0,
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
    @SerializedName("id") var id: String = "",
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
    @SerializedName("date") var date: String = "",
    @SerializedName("bus_trip") var busTrip: BusTrip,
    @SerializedName("user") var user: User,
    @SerializedName("taxi") var taxi: Taxi,
    @SerializedName("price") var price: Double,
    @SerializedName("taxi_rating") var taxiRating: Double,
    @SerializedName("user_rating") var userRating: Double
) : JSONConvertable, Parcelable

