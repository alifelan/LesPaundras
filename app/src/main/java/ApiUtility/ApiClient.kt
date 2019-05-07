package ApiUtility

import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import org.json.JSONObject

class ApiClient(private val ctx: Context) {

    /***
     * PERFORM REQUEST
     */
    private fun performRequest(route: ApiRoute, completion: (success: Boolean, apiResponse: ApiResponse) -> Unit) {
        val request: JsonObjectRequest = object : JsonObjectRequest(route.httpMethod, route.url, route.body, { response ->
            this.handle(response, completion)
        }, {
            it.printStackTrace()
            if (it.networkResponse != null && it.networkResponse.data != null)
                this.handle(JSONObject().apply {
                    put("message", JSONObject(String(it.networkResponse.data)).optString("message"))
                    put("status", "false")}, completion)
            else
                this.handle(JSONObject().apply {
                    put("message", getStringError(it))
                    put("status", "false")}, completion)
        }) {
            override fun getHeaders(): MutableMap<String, String> = route.headers
        }
        request.retryPolicy = DefaultRetryPolicy(route.timeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        getRequestQueue().add(request)
    }

    /**
     * This method will make the creation of the answer as ApiResponse
     **/
    private fun handle(response: JSONObject, completion: (success: Boolean, apiResponse: ApiResponse) -> Unit) {
        val ar = ApiResponse(response)
        completion.invoke(ar.success, ar)
    }

    /**
     * This method will return the error as String
     **/
    private fun getStringError(volleyError: VolleyError): String {
        return when (volleyError) {
            is TimeoutError -> "The conection timed out."
            is NoConnectionError -> "The conection couldn´t be established."
            is AuthFailureError -> "There was an authentication failure in your request."
            is ServerError -> "Error while prosessing the server response."
            is NetworkError -> "Network error, please verify your conection."
            is ParseError -> "Error while prosessing the server response."
            else -> "Internet error"
        }
    }
    /**
     * We create and return a new instance for the queue of Volley requests.
     **/
    private fun getRequestQueue(): RequestQueue {
        val maxCacheSize = 20 * 1024 * 1024
        val cache = DiskBasedCache(ctx.cacheDir, maxCacheSize)
        val netWork = BasicNetwork(HurlStack())
        val mRequestQueue = RequestQueue(cache, netWork)
        mRequestQueue.start()
        System.setProperty("http.keepAlive", "false")
        return mRequestQueue
    }

    fun getRandomBusTrip(completion: (randomTrip: String?, message: String) -> Unit) {
        val route = ApiRoute.RandomBusTrip(ctx)
        this.performRequest(route) {success, response ->
            val id = response.json.getString("id")
            if(success)
                completion.invoke(id, "")
            else
                completion.invoke(null, response.message)
        }
    }

    fun login(email: String, password: String, completion: (logged: Boolean, message: String) -> Unit) {
        val route = ApiRoute.Login(email, password,ctx)
        this.performRequest(route) { success, response ->
            completion.invoke(success, response.message)
        }
    }

    fun createUser(name: String, email: String, password: String, card: String, completion: (user: User?, status: Boolean, message: String) -> Unit) {
        val route = ApiRoute.User(name, email, password, card, ctx)
        this.performRequest(route) { success, response ->
            if(success) {
                val user = Gson().fromJson(response.json.toString(), User::class.java)
                completion.invoke(user, success, "User registration completed")
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }

    fun getUser(email : String, completion: (user: User?, status: Boolean, message: String) -> Unit) {
        val route = ApiRoute.UserData(email, ctx)
        this.performRequest(route) { success, response ->
            if(success) {
                val user = Gson().fromJson(response.json.toString(), User::class.java)
                completion.invoke(user, success, "User GET success")
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }

    fun updateUser(name: String, email: String, password: String, card: String, completion: (user: User?, status: Boolean, message: String) -> Unit) {
        val route = ApiRoute.UpdateUser(name, email, password, card, ctx)
        this.performRequest(route) { success, response ->
            if(success) {
                val user = Gson().fromJson(response.json.toString(), User::class.java)
                completion.invoke(user, success, "User update completed")
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }

    fun getBusTrip(id: String, completion:(trip: BusTrip?, status: Boolean, message:String) -> Unit) {
        val route = ApiRoute.GetBusTrip(id, ctx)
        this.performRequest(route) { success, response ->
            if(success) {
                val trip = Gson().fromJson(response.json.toString(), BusTrip::class.java)
                completion.invoke(trip, success, "Trip retrieved successfully")
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }

    fun getCoordinates(address: String, completion:(coord: Coordinates?, status: Boolean, message:String) -> Unit) {
        val route = ApiRoute.GetGeoCoding(address, ctx)
        this.performRequest(route) { success, response ->
            if(success) {
                val geometry = response.json.getJSONObject("geometry")
                val location = geometry.getJSONObject("location")
                val lat = location.getDouble("lat")
                val lng = location.getDouble("lng")
                val coord = Coordinates(lat, lng)
                completion.invoke(coord, success, "Geocoding complete")
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }

    fun getDirections(origin: String, destination: String, completion:(polylines: MutableList<String>?, status: Boolean, message: String) -> Unit) {
        val route = ApiRoute.GetDirections(origin, destination, ctx)
        this.performRequest(route) { success, response ->
            if(success) {
                val routes = response.json.getJSONArray("routes")
                if(routes.length() == 0) {
                    completion.invoke(null, false, "No routes found")
                } else {
                    val legs = routes.getJSONObject(0).getJSONArray("legs")
                    val polylines: MutableList<String> = mutableListOf()
                    for(i in 0 until legs.length()) {
                        val steps = legs.getJSONArray(i)
                        for(j in 0 until steps.length()) {
                            polylines.add(steps.getJSONObject(j).getJSONObject("polyline").getString("points"))
                        }
                    }
                    completion.invoke(polylines, success, "Route found")
                }
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }


}