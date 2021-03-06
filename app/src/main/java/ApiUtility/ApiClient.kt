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

import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

/**
 * Class in charge of actually handling the http requests
 */
class ApiClient(private val ctx: Context) {

    /***
     * Make api call
     */
    private fun performRequest(route: ApiRoute, completion: (success: Boolean, apiResponse: ApiResponse) -> Unit) {
        val request: JsonObjectRequest =
            object : JsonObjectRequest(route.httpMethod, route.url, route.body, { response ->
                this.handle(response, completion)
            }, {
                it.printStackTrace()
                if (it.networkResponse != null && it.networkResponse.data != null)
                    this.handle(JSONObject().apply {
                        put("message", JSONObject(String(it.networkResponse.data)).optString("message"))
                        put("status", "false")
                    }, completion)
                else
                    this.handle(JSONObject().apply {
                        put("message", getStringError(it))
                        put("status", "false")
                    }, completion)
            }) {
                override fun getHeaders(): MutableMap<String, String> = route.headers
            }
        request.retryPolicy = DefaultRetryPolicy(
            route.timeOut,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        getRequestQueue().add(request)
    }

    /**
     * Creates ApiResponse to be used
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

    /**
     * Get a random bus trip id
     */
    fun getRandomBusTrip(completion: (randomTrip: String?, message: String) -> Unit) {
        val route = ApiRoute.RandomBusTrip(ctx)
        this.performRequest(route) { success, response ->
            val id = response.json.getString("id")
            if (success)
                completion.invoke(id, "")
            else
                completion.invoke(null, response.message)
        }
    }

    /**
     * Login user to the application
     */
    fun login(email: String, password: String, completion: (logged: Boolean, message: String) -> Unit) {
        val route = ApiRoute.Login(email, password, ctx)
        this.performRequest(route) { success, response ->
            completion.invoke(success, response.message)
        }
    }

    /**
     * Used to register users in the application
     */
    fun createUser(
        name: String,
        email: String,
        password: String,
        card: String,
        completion: (user: User?, status: Boolean, message: String) -> Unit
    ) {
        val route = ApiRoute.User(name, email, password, card, ctx)
        this.performRequest(route) { success, response ->
            if (success) {
                val user = Gson().fromJson(response.json.toString(), User::class.java)
                completion.invoke(user, success, "User registration completed")
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }

    /**
     * Get user information
     */
    fun getUser(email: String, completion: (user: User?, status: Boolean, message: String) -> Unit) {
        val route = ApiRoute.UserData(email, ctx)
        this.performRequest(route) { success, response ->
            if (success) {
                val user = Gson().fromJson(response.json.toString(), User::class.java)
                completion.invoke(user, success, "User GET success")
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }

    /**
     * Update user
     */
    fun updateUser(
        name: String,
        email: String,
        password: String,
        card: String,
        completion: (user: User?, status: Boolean, message: String) -> Unit
    ) {
        val route = ApiRoute.UpdateUser(name, email, password, card, ctx)
        this.performRequest(route) { success, response ->
            if (success) {
                val user = Gson().fromJson(response.json.toString(), User::class.java)
                completion.invoke(user, success, "User update completed")
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }

    /**
     * Get a bustrip by an id
     */
    fun getBusTrip(id: String, completion: (trip: BusTrip?, status: Boolean, message: String) -> Unit) {
        val route = ApiRoute.GetBusTrip(id, ctx)
        this.performRequest(route) { success, response ->
            if (success) {
                val trip = Gson().fromJson(response.json.toString(), BusTrip::class.java)
                completion.invoke(trip, success, "Trip retrieved successfully")
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }

    /**
     * Call google maps api to get coordinates from an address
     */
    fun getCoordinates(address: String, completion: (coord: LatLng?, status: Boolean, message: String) -> Unit) {
        val route = ApiRoute.GetGeoCoding(address, ctx)
        this.performRequest(route) { success, response ->
            if (success && response.json.getString("status") == "OK") {
                val results = response.json.getJSONArray("results")
                val geometry = results.getJSONObject(0).getJSONObject("geometry")
                val location = geometry.getJSONObject("location")
                val lat = location.getDouble("lat")
                val lng = location.getDouble("lng")
                val coord = LatLng(lat, lng)
                completion.invoke(coord, success, "Geocoding complete")
            } else {
                completion.invoke(null, false, response.message)
            }
        }
    }

    /**
     * Used to decode polylines received from the google directions api
     */
    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly
    }

    /**
     * Call to the google directions api
     */
    fun getDirections(
        origin: String,
        destination: String,
        completion: (route: Route?, status: Boolean, message: String) -> Unit
    ) {
        val route = ApiRoute.GetDirections(origin, destination, ctx)
        this.performRequest(route) { success, response ->
            if (success) {
                val routes = response.json.getJSONArray("routes")
                if (routes.length() == 0) {
                    completion.invoke(null, false, "No routes found")
                } else {
                    val route = routes.getJSONObject(0)
                    val legs = route.getJSONArray("legs")
                    var du = 0
                    var di = 0
                    var du_text = ""
                    var di_text = ""
                    val points: MutableList<LatLng> = mutableListOf()
                    for (i in 0 until legs.length()) {
                        val leg = legs.getJSONObject(i)
                        val steps = leg.getJSONArray("steps")
                        val duration: ValueText =
                            Gson().fromJson(leg.getJSONObject("duration").toString(), ValueText::class.java)
                        du += duration.value
                        du_text = duration.text
                        val distance: ValueText =
                            Gson().fromJson(leg.getJSONObject("distance").toString(), ValueText::class.java)
                        di += distance.value
                        di_text = distance.text
                        for (j in 0 until steps.length()) {
                            val poly = decodePoly(steps.getJSONObject(j).getJSONObject("polyline").getString("points"))
                            points.addAll(poly)
                        }
                    }
                    completion.invoke(Route(points, ValueText(du, du_text), ValueText(di, di_text)), success, "Route found")
                }
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }

    /**
     * Create a taxi trip in the application
     */
    fun createTaxiTrip(
        email: String,
        busTripId: String,
        state: String,
        city: String,
        address: String,
        latlng : LatLng,
        trip: Int,
        price: Double,
        distance: ValueText,
        duration: ValueText,
        completion: (trip: TaxiTrip?, status: Boolean, message: String) -> Unit
    ) {
        val route = ApiRoute.CreateTaxiTrip(email, busTripId, state, city, address, latlng, trip, price, distance, duration,ctx)
        this.performRequest(route) {success, response ->
            if(success) {
                val trip: TaxiTrip = Gson().fromJson(response.json.toString(), TaxiTrip::class.java)
                completion.invoke(trip, success, "Created taxi trip")
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }

    /**
     * Get the closest trip that the user takes
     * If user has a current trip, return it
     * If it doesn't, return the closest pending trip
     */
    fun getCurrentOrNextTrip(email: String, completion: (trip: TaxiTrip?, rate: TaxiTrip? ,current: Boolean, status: Boolean, message: String) -> Unit) {
        val route = ApiRoute.GetCurrentOrNext(email, ctx)
        this.performRequest(route) {success, response ->
            if(success && response.json.optJSONObject("taxi_trip") != null) {
                val current = response.json.getBoolean("current")
                val trip: TaxiTrip = Gson().fromJson(response.json.getJSONObject("taxi_trip").toString(), TaxiTrip::class.java)
                var rate: TaxiTrip? = null
                if(response.json.getJSONArray("rate").length() > 0) {
                    rate = Gson().fromJson(response.json.getJSONArray("rate").getJSONObject(0).toString(), TaxiTrip::class.java)
                }
                completion.invoke(trip, rate, current, success, response.message)
            } else {
                completion.invoke(null, null,false, success, "Unable to get trip")
            }
        }
    }

    /**
     * returns all the trips of the user (for the trip records)
     */
    fun getUserTaxiTrips(email: String, completion: (trips: UserTaxiTrips?, status: Boolean, message: String) -> Unit) {
        val route = ApiRoute.GetUserTaxiTrips(email, ctx)
        this.performRequest(route) {success, response ->
            if(success) {
                val trips: UserTaxiTrips = Gson().fromJson(response.json.toString(), UserTaxiTrips::class.java)
                completion.invoke(trips, success, "Got all user trips")
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }

    /**
     * Post method that cancels a trip
     */
    fun cancelTaxiTrip(tripId: String, completion: (trip: TaxiTrip?, status: Boolean, message: String) -> Unit) {
        val route = ApiRoute.CancelTrip(tripId, ctx)
        this.performRequest(route) {success, response ->
            if(success) {
                val trip: TaxiTrip = Gson().fromJson(response.json.toString(), TaxiTrip::class.java)
                completion.invoke(trip, success, "Trip cancelled")
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }

    /**
     * Post method to rate driver from trip
     */
    fun rateDriver(email: String, rating: Float, completion: (trip : TaxiTrip?, success: Boolean, message: String) -> Unit) {
        val route = ApiRoute.RateDriver(email, rating, ctx)
        this.performRequest(route) {success, response ->
            if(success) {
                val trip: TaxiTrip = Gson().fromJson(response.json.toString(), TaxiTrip::class.java)
                completion.invoke(trip, success, "Successful rating")
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }

    /**
     * Get the trips belonging to a certain bus ticket id
     * e.g. the rides from source to bus terminal A, from terminal A to dest
     */
    fun getUserBusTrips(busTripId: String, email: String, completion: (trip1: TaxiTrip?, trip2: TaxiTrip?, trip3: TaxiTrip? , trip4: TaxiTrip?, status: Boolean, message: String) -> Unit) {
        val route = ApiRoute.GetUserBusTrips(busTripId, email, ctx)
        this.performRequest(route) {success, response ->
            if(success) {
                val trip1: TaxiTrip? = if(response.json.getJSONArray("trip1").length() > 0) Gson().fromJson(response.json.getJSONArray("trip1").getJSONObject(0).toString(), TaxiTrip::class.java) else null
                val trip2: TaxiTrip? = if(response.json.getJSONArray("trip2").length() > 0) Gson().fromJson(response.json.getJSONArray("trip2").getJSONObject(0).toString(), TaxiTrip::class.java) else null
                val trip3: TaxiTrip? = if(response.json.getJSONArray("trip3").length() > 0) Gson().fromJson(response.json.getJSONArray("trip3").getJSONObject(0).toString(), TaxiTrip::class.java) else null
                val trip4: TaxiTrip? = if(response.json.getJSONArray("trip4").length() > 0) Gson().fromJson(response.json.getJSONArray("trip4").getJSONObject(0).toString(), TaxiTrip::class.java) else null
                completion.invoke(trip1, trip2, trip3, trip4, success, "User rides fetched")
            } else {
                completion.invoke(null, null, null, null, success, response.message)
            }
        }
    }

    fun updateTaxiTripAddress(tripId: String, name: String, state: String, city: String, address: String, latlng: LatLng, completion: (trip: TaxiTrip?, success: Boolean, message: String) -> Unit) {
        val route = ApiRoute.UpdateTripAddress(tripId, name, state, city ,address, latlng, ctx)
        this.performRequest(route) {success, response ->
            if(success) {
                val trip: TaxiTrip = Gson().fromJson(response.json.toString(), TaxiTrip::class.java)
                completion.invoke(trip, success, "Address updated")
            } else {
                completion.invoke(null, success, response.message)
            }
        }
    }
}