
package se.sugarest.jane.viaplaysections.data.type;

import com.google.gson.annotations.SerializedName;

public class JSONResponse {
    @SerializedName("_links")
    private Links links;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }
}
