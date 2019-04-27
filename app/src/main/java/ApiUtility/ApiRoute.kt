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

    data class Login(var email: String, var password:String, var ctx: Context): ApiRoute()
    data class RandomBusTrip(var ctx: Context): ApiRoute()

    val url: String
        get() {
            return "$baseUrl/${when (this@ApiRoute) {
                is RandomBusTrip -> "randomBusTrip"
                is Login -> "login/"
            }}"
        }
    val httpMethod: Int
        get() {
            return when (this) {
                is RandomBusTrip -> Request.Method.GET
                is Login -> Request.Method.POST
            }
        }

    val body: JSONObject
        get() {
            return when (this) {
                is RandomBusTrip -> JSONObject()
                is Login -> {
                    val json = JSONObject()
                    json.put("email", this.email)
                    json.put("password", this.password)
                    json
                }
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