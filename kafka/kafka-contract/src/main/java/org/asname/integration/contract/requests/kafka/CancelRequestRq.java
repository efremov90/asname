
package org.asname.integration.contract.requests.kafka;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Header",
    "CancelRequest"
})
public class CancelRequestRq {

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
    @JsonProperty("CancelRequest")
    private CancelRequest cancelRequest;
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
    @JsonProperty("CancelRequest")
    public CancelRequest getCancelRequest() {
        return cancelRequest;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("CancelRequest")
    public void setCancelRequest(CancelRequest cancelRequest) {
        this.cancelRequest = cancelRequest;
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
        return new HashCodeBuilder().append(header).append(cancelRequest).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CancelRequestRq) == false) {
            return false;
        }
        CancelRequestRq rhs = ((CancelRequestRq) other);
        return new EqualsBuilder().append(header, rhs.header).append(cancelRequest, rhs.cancelRequest).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
