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
            return "https://taxi-unico-api.herokuapp.com"
        }

    data class Login(var email: String, var password:String, var ctx: Context): ApiRoute()
    data class RandomBusTrip(var ctx: Context): ApiRoute()

    val url: String
        get() {
            return "$baseUrl/${when (this@ApiRoute) {
                is RandomBusTrip -> "randomBusTrip"
                is Login -> "login"
            }}"
        }
    val httpMethod: Int
        get() {
            return when (this) {
                is RandomBusTrip -> Request.Method.GET
                is Login -> Request.Method.GET
            }
        }

    val params: JSONObject
        get() {
            return when (this) {
                is Login -> JSONObject()
                is RandomBusTrip -> JSONObject()
                is Login -> {
            }
        }

    val headers: HashMap<String, String>
        get() {
            val map: HashMap<String, String> = hashMapOf()
            map["Accept"] = "application/json"
            map["Content-Type"] = "application/json"
            return map
        }

}