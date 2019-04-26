package ApiUtility

import org.json.JSONObject

class ApiResponse(public var json: JSONObject) {

    var message: String = ""
    var success: Boolean = true

    init {
        if(json.has("status")) {
            message = json.getString("status")
            success = message != "false"
        }
    }

}
