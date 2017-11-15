
package se.sugarest.jane.viaplaysections.data.type;

import java.util.HashMap;
import java.util.Map;

public class ResponseCode {

    private Integer httpStatus;
    private Integer statusCode;
    private Integer code;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
