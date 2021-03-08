
package org.asname.integration.contract.requests.kafka;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "RqUID",
    "RqTm",
    "correlationUID"
})
public class Header {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("RqUID")
    private String rqUID;
    /**
     * Дата и время передачи сообщения
     * (Required)
     * 
     */
    @JsonProperty("RqTm")
    @JsonPropertyDescription("\u0414\u0430\u0442\u0430 \u0438 \u0432\u0440\u0435\u043c\u044f \u043f\u0435\u0440\u0435\u0434\u0430\u0447\u0438 \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u044f")
    private Date rqTm;
    @JsonProperty("correlationUID")
    private String correlationUID;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("RqUID")
    public String getRqUID() {
        return rqUID;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("RqUID")
    public void setRqUID(String rqUID) {
        this.rqUID = rqUID;
    }

    /**
     * Дата и время передачи сообщения
     * (Required)
     * 
     */
    @JsonProperty("RqTm")
    public Date getRqTm() {
        return rqTm;
    }

    /**
     * Дата и время передачи сообщения
     * (Required)
     * 
     */
    @JsonProperty("RqTm")
    public void setRqTm(Date rqTm) {
        this.rqTm = rqTm;
    }

    @JsonProperty("correlationUID")
    public String getCorrelationUID() {
        return correlationUID;
    }

    @JsonProperty("correlationUID")
    public void setCorrelationUID(String correlationUID) {
        this.correlationUID = correlationUID;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(rqUID).append(rqTm).append(correlationUID).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Header) == false) {
            return false;
        }
        Header rhs = ((Header) other);
        return new EqualsBuilder().append(rqUID, rhs.rqUID).append(rqTm, rhs.rqTm).append(correlationUID, rhs.correlationUID).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
