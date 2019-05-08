package ApiUtility

import org.json.JSONObject

class ApiResponse(public var json: JSONObject) {

    var message: String = ""
    var success: Boolean = true

    init {
        if(json.has("status")) {
            if(json.optString("status") != null) {
                success = json.getString("status") != "false"
            }
            if(json.has("message")) {
                message = json.getString("message")
            }
        }
    }

}
