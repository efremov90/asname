
package org.asname.integration.contract.requests.kafka;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "RequestUUID",
    "Comment"
})
public class CancelRequest {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("RequestUUID")
    private String requestUUID;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Comment")
    private String comment;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("RequestUUID")
    public String getRequestUUID() {
        return requestUUID;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("RequestUUID")
    public void setRequestUUID(String requestUUID) {
        this.requestUUID = requestUUID;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Comment")
    public String getComment() {
        return comment;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("Comment")
    public void setComment(String comment) {
        this.comment = comment;
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
        return new HashCodeBuilder().append(requestUUID).append(comment).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CancelRequest) == false) {
            return false;
        }
        CancelRequest rhs = ((CancelRequest) other);
        return new EqualsBuilder().append(requestUUID, rhs.requestUUID).append(comment, rhs.comment).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
