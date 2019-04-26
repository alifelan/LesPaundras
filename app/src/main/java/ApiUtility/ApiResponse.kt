package ApiUtility

import org.json.JSONObject

class ApiResponse(public var json: JSONObject) {

    var message: String = ""
    var success: Boolean = true

    init {
        if(json.has("message")) {
            message = json.getString("message")
            success = false
        }
    }

}
