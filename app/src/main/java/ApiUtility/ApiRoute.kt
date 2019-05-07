package ApiUtility

import android.content.Context
import com.android.volley.Request
import org.json.JSONObject
import kotlin.collections.HashMap


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
                )}&destination=${this.destination.replace(' ', '+')}&key=$API_KEY"
            }
        }
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
            }
        }

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
            }
        }

    val headers: HashMap<String, String>
        get() {
            val map: HashMap<String, String> = hashMapOf()
            map["Accept"] = "application/json"
            map["Content-Type"] = "application/json; charset=utf-8"
            return map
        }

}