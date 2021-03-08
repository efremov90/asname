
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
    "CreateDate",
    "ClientCode",
    "Comment"
})
public class CreateRequest {

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
    @JsonProperty("CreateDate")
    private String createDate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("ClientCode")
    private String clientCode;
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
    @JsonProperty("CreateDate")
    public String getCreateDate() {
        return createDate;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("CreateDate")
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("ClientCode")
    public String getClientCode() {
        return clientCode;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("ClientCode")
    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
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
        return new HashCodeBuilder().append(requestUUID).append(createDate).append(clientCode).append(comment).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CreateRequest) == false) {
            return false;
        }
        CreateRequest rhs = ((CreateRequest) other);
        return new EqualsBuilder().append(requestUUID, rhs.requestUUID).append(createDate, rhs.createDate).append(clientCode, rhs.clientCode).append(comment, rhs.comment).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
