package ApiUtility

import org.json.JSONObject

class ApiResponse(public var json: JSONObject) {

    var message: String = ""
    var success: Boolean = true

    private val data = "data"
    private val msg = "error"

    init {
        if(json.has("message")) {
            message = json.getString("message")
            success = false
        }
    }

}
