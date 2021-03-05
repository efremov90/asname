
package org.asname.integration.contract.requests.kafkaone;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Header",
    "Status"
})
public class CancelRequestRs {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Header")
    private Header header;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Status")
    private Status status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Header")
    public Header getHeader() {
        return header;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Header")
    public void setHeader(Header header) {
        this.header = header;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Status")
    public Status getStatus() {
        return status;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Status")
    public void setStatus(Status status) {
        this.status = status;
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
        return new HashCodeBuilder().append(header).append(status).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CancelRequestRs) == false) {
            return false;
        }
        CancelRequestRs rhs = ((CancelRequestRs) other);
        return new EqualsBuilder().append(header, rhs.header).append(status, rhs.status).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
