package ApiUtility

import org.json.JSONObject
import org.json.JSONTokener

class ApiResponse(response: String) {

    var success: Boolean = false
    var message: String = ""
    var json: JSONObject = JSONObject()

    private val data = "data"
    private val msg = "error"

    init {
        try {
            val jsonToken = JSONTokener(response).nextValue()
            if (jsonToken is JSONObject) {
                json = JSONObject(response)

                message = if (json.has(msg)) {
                    json.getJSONObject(msg).getString("message")
                } else {
                    "An error occurred while processing the response"
                }

                if (json.optJSONObject(data) != null) {
                    json = json.getJSONObject(data)
                    success = true
                } else {
                    success = false
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
