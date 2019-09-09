package se.sugarest.jane.viaplaysections.data.datatype

import com.google.gson.annotations.SerializedName

class JSONResponse {
    @SerializedName("_links")
    var links: Links? = null
}
