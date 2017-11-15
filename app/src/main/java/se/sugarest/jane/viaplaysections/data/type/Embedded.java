
package se.sugarest.jane.viaplaysections.data.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Embedded {

    private List<Object> viaplayBlocks = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<Object> getViaplayBlocks() {
        return viaplayBlocks;
    }

    public void setViaplayBlocks(List<Object> viaplayBlocks) {
        this.viaplayBlocks = viaplayBlocks;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
