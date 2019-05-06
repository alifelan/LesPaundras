package ApiUtility

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("name") var name: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("rating") var rating : Int = 0,
    @SerializedName("trips") var trips : Int = 0
) : JSONConvertable

data class Place(
    @SerializedName("id") var id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("state") var state: String = "",
    @SerializedName("city") var city: String = "",
    @SerializedName("address") var address: String = ""
) : JSONConvertable

data class BusTrip(
    @SerializedName("id") var id: String = "",
    @SerializedName("origin") var origin: Place,
    @SerializedName("destination") var destination: Place,
    @SerializedName("departure_date") var departure_date: String = "",
    @SerializedName("arrival_date") var arrival_date: String = ""
)

