package ApiUtility

import android.content.Context
import com.android.volley.Request
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import java.time.Duration
import kotlin.collections.HashMap

/**
 * Class in charge of holding routes for api calls
 */
sealed class ApiRoute {

    val timeOut: Int
        get() {
            return 3000
        }

    val baseUrl: String
        get() {
            return "http://taxi-unico-api.herokuapp.com"
        }

    val geoCodingUrl: String
        get() {
            return "https://maps.googleapis.com/maps/api/geocode"
        }

    val directionsUrl: String
        get() {
            return "https://maps.googleapis.com/maps/api/directions"
        }

    val API_KEY: String = "AIzaSyDQVUiK_t-ui74iRfPc3phKkJltx6YO4bc"

    data class Login(var email: String, var password: String, var ctx: Context) : ApiRoute()
    data class RandomBusTrip(var ctx: Context) : ApiRoute()
    data class User(var name: String, var email: String, var password: String, var card: String, var ctx: Context) :
        ApiRoute()

    data class UpdateUser(
        var name: String,
        var email: String,
        var password: String,
        var card: String,
        var ctx: Context
    ) : ApiRoute()

    data class UserData(var email: String, var ctx: Context) : ApiRoute()
    data class GetBusTrip(var id: String, var ctx: Context) : ApiRoute()
    data class GetGeoCoding(var adddress: String, var ctx: Context) : ApiRoute()
    data class GetDirections(var origin: String, var destination: String, var ctx: Context) : ApiRoute()
    data class CreateTaxiTrip(var email: String, var busTripId: String, var state: String, var city: String, var address: String, var latlng: LatLng,var trip: Int, var price: Double, var distance: ValueText, var duration: ValueText, var ctx: Context) : ApiRoute()
    data class GetCurrentOrNext(var email: String, var ctx: Context) : ApiRoute()

    /**
     * Url to be used for the api call
     */
    val url: String
        get() {
            return when (this) {
                is RandomBusTrip -> "$baseUrl/randomBusTrip"
                is Login -> "$baseUrl/login/"
                is User -> "$baseUrl/user/"
                is UpdateUser -> "$baseUrl/user/"
                is UserData -> "$baseUrl/user/${this.email}"
                is GetBusTrip -> "$baseUrl/busTrip/${this.id}"
                is GetGeoCoding -> "$geoCodingUrl/json?address=${this.adddress.replace(' ', '+')}&key=$API_KEY"
                is GetDirections -> "$directionsUrl/json?origin=${this.origin.replace(
                    ' ',
                    '+'
                )}&destination=${this.destination.replace(' ', '+')}&units=metric&key=$API_KEY"
                is CreateTaxiTrip -> "$baseUrl/createTaxiTrip/"
                is GetCurrentOrNext -> "$baseUrl/getCurrentOrNext/${this.email}"
            }
        }

    /**
     * Method for the api call
     */
    val httpMethod: Int
        get() {
            return when (this) {
                is RandomBusTrip -> Request.Method.GET
                is Login -> Request.Method.POST
                is User -> Request.Method.POST
                is UpdateUser -> Request.Method.PUT
                is UserData -> Request.Method.GET
                is GetBusTrip -> Request.Method.GET
                is GetGeoCoding -> Request.Method.GET
                is GetDirections -> Request.Method.GET
                is CreateTaxiTrip -> Request.Method.POST
                is GetCurrentOrNext -> Request.Method.GET
            }
        }

    /**
     * Body to be sent in the api call
     */
    val body: JSONObject?
        get() {
            return when (this) {
                is RandomBusTrip -> null
                is Login -> {
                    val json = JSONObject()
                    json.put("email", this.email)
                    json.put("password", this.password)
                }
                is User -> {
                    val json = JSONObject()
                    json.put("name", this.name)
                    json.put("email", this.email)
                    json.put("password", this.password)
                    json.put("card", this.card)
                }
                is UpdateUser -> {
                    val json = JSONObject()
                    json.put("name", this.name)
                    json.put("email", this.email)
                    if (this.password != "") {
                        json.put("password", this.password)
                    }
                    if (this.card != "") {
                        json.put("card", this.card)
                    }
                    json
                }
                is UserData -> null
                is GetBusTrip -> null
                is GetGeoCoding -> null
                is GetDirections -> null
                is CreateTaxiTrip -> {
                    val json = JSONObject()
                    json.put("email", this.email)
                    json.put("busTripId", this.busTripId)
                    json.put("trip", this.trip)
                    json.put("price", this.price)
                    json.put("distance", JSONObject().apply{
                        put("value", this@ApiRoute.distance.value)
                        put("text", this@ApiRoute.distance.text)
                    })
                    json.put("duration", JSONObject().apply {
                        put("value", this@ApiRoute.duration.value)
                        put("text", this@ApiRoute.duration.text)
                    })
                    json.put("location", JSONObject().apply{
                        put("name", this@ApiRoute.address)
                        put("state", this@ApiRoute.state)
                        put("city", this@ApiRoute.city)
                        put("address", this@ApiRoute.address)
                        put("latitude", this@ApiRoute.latlng.latitude)
                        put("longitude", this@ApiRoute.latlng.longitude)
                    })
                }
                is GetCurrentOrNext -> null
            }
        }

    /**
     * Headers for every call
     */
    val headers: HashMap<String, String>
        get() {
            val map: HashMap<String, String> = hashMapOf()
            map["Accept"] = "application/json"
            map["Content-Type"] = "application/json; charset=utf-8"
            return map
        }

}