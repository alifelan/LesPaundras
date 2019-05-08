package ApiUtility

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class User(
    @SerializedName("name") var name: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("rating") var rating : Int = 0,
    @SerializedName("trips") var trips : Int = 0
) : JSONConvertable

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

