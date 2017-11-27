
package se.sugarest.jane.viaplaysections.data.datatype;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Links {
    @SerializedName("viaplay:sections")
    private List<ViaplaySection> viaplaySections = null;

    public List<ViaplaySection> getViaplaySections() {
        return viaplaySections;
    }

    public void setViaplaySections(List<ViaplaySection> viaplaySections) {
        this.viaplaySections = viaplaySections;
    }
}
