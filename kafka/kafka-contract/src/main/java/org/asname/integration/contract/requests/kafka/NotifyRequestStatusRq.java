
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
    "NotifyRequestStatusRequest"
})
public class NotifyRequestStatusRq {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Header")
    private Header header;
    @JsonProperty("NotifyRequestStatusRequest")
    private NotifyRequestStatusRequest notifyRequestStatusRequest;
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

    @JsonProperty("NotifyRequestStatusRequest")
    public NotifyRequestStatusRequest getNotifyRequestStatusRequest() {
        return notifyRequestStatusRequest;
    }

    @JsonProperty("NotifyRequestStatusRequest")
    public void setNotifyRequestStatusRequest(NotifyRequestStatusRequest notifyRequestStatusRequest) {
        this.notifyRequestStatusRequest = notifyRequestStatusRequest;
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
        return new HashCodeBuilder().append(header).append(notifyRequestStatusRequest).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof NotifyRequestStatusRq) == false) {
            return false;
        }
        NotifyRequestStatusRq rhs = ((NotifyRequestStatusRq) other);
        return new EqualsBuilder().append(header, rhs.header).append(notifyRequestStatusRequest, rhs.notifyRequestStatusRequest).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
