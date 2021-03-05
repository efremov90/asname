
package org.asname.integration.contract.requests.kafkaone;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Header",
    "CreateRequest"
})
public class CreateRequestRq {

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
    @JsonProperty("CreateRequest")
    private CreateRequest createRequest;
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
    @JsonProperty("CreateRequest")
    public CreateRequest getCreateRequest() {
        return createRequest;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("CreateRequest")
    public void setCreateRequest(CreateRequest createRequest) {
        this.createRequest = createRequest;
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
        return new HashCodeBuilder().append(header).append(createRequest).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CreateRequestRq) == false) {
            return false;
        }
        CreateRequestRq rhs = ((CreateRequestRq) other);
        return new EqualsBuilder().append(header, rhs.header).append(createRequest, rhs.createRequest).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
