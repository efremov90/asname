
package org.asname.integration.contract.requests.kafkaone;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "RequestUUID",
    "Status",
    "Comment"
})
public class NotifyRequestStatusRequest {

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
    @JsonProperty("Status")
    private Status status;
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

    @JsonProperty("Comment")
    public String getComment() {
        return comment;
    }

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
        return new HashCodeBuilder().append(requestUUID).append(status).append(comment).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof NotifyRequestStatusRequest) == false) {
            return false;
        }
        NotifyRequestStatusRequest rhs = ((NotifyRequestStatusRequest) other);
        return new EqualsBuilder().append(requestUUID, rhs.requestUUID).append(status, rhs.status).append(comment, rhs.comment).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    public enum Status {

        CREATED("CREATED"),
        CANCELED("CANCELED"),
        CLOSED("CLOSED"),
        ERROR("ERROR");
        private final String value;
        private final static Map<String, Status> CONSTANTS = new HashMap<String, Status>();

        static {
            for (Status c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Status fromValue(String value) {
            Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
