package ApiUtility

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("name") var name: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("rating") var rating : Int = 0,
    @SerializedName("trips") var trips : Int = 0
) : JSONConvertable, Parcelable

@Parcelize
data class Place(
    @SerializedName("id") var id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("state") var state: String = "",
    @SerializedName("city") var city: String = "",
    @SerializedName("address") var address: String = ""
) : JSONConvertable, Parcelable

@Parcelize
data class BusTrip(
    @SerializedName("id") var id: String = "",
    @SerializedName("origin") var origin: Place,
    @SerializedName("destination") var destination: Place,
    @SerializedName("departure_date") var departure_date: String = "",
    @SerializedName("arrival_date") var arrival_date: String = "",
    @SerializedName("roundtrip") var roundtrip: Boolean
) : JSONConvertable, Parcelable

@Parcelize
data class ValueText(
    @SerializedName("value") var value: Int = 0,
    @SerializedName("text") var text: String = ""
): JSONConvertable, Parcelable

@Parcelize
data class Route(
    var points: List<LatLng>,
    var duration: ValueText,
    var distance: ValueText
) : Parcelable

@Parcelize
data class Address(
    var address: String,
    var coordinates: LatLng
) : Parcelable

@Parcelize
data class Taxi(
    @SerializedName("id") var id: String = "",
    @SerializedName("driver_name") var driverName: String = "",
    @SerializedName("plate") var plate: String = "",
    @SerializedName("model") var model: String = "",
    @SerializedName("brand") var brand: String = "",
    @SerializedName("taxi_number") var taxi_number: String = ""
) : JSONConvertable, Parcelable

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

