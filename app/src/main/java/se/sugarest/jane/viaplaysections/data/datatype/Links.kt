package se.sugarest.jane.viaplaysections.data.datatype

import com.google.gson.annotations.SerializedName

class Links {
    @SerializedName("viaplay:sections")
    var viaplaySections: List<ViaplaySection>? = null
}
