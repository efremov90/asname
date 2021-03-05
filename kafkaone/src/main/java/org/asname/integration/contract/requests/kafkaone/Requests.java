
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
    "CreateRequestRq",
    "CreateRequestRs",
    "CancelRequestRq",
    "CancelRequestRs",
    "NotifyRequestStatusRq",
    "NotifyRequestStatusRs"
})
public class Requests {

    @JsonProperty("CreateRequestRq")
    private CreateRequestRq createRequestRq;
    @JsonProperty("CreateRequestRs")
    private CreateRequestRs createRequestRs;
    @JsonProperty("CancelRequestRq")
    private CancelRequestRq cancelRequestRq;
    @JsonProperty("CancelRequestRs")
    private CreateRequestRs cancelRequestRs;
    @JsonProperty("NotifyRequestStatusRq")
    private NotifyRequestStatusRq notifyRequestStatusRq;
    @JsonProperty("NotifyRequestStatusRs")
    private CreateRequestRs notifyRequestStatusRs;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("CreateRequestRq")
    public CreateRequestRq getCreateRequestRq() {
        return createRequestRq;
    }

    @JsonProperty("CreateRequestRq")
    public void setCreateRequestRq(CreateRequestRq createRequestRq) {
        this.createRequestRq = createRequestRq;
    }

    @JsonProperty("CreateRequestRs")
    public CreateRequestRs getCreateRequestRs() {
        return createRequestRs;
    }

    @JsonProperty("CreateRequestRs")
    public void setCreateRequestRs(CreateRequestRs createRequestRs) {
        this.createRequestRs = createRequestRs;
    }

    @JsonProperty("CancelRequestRq")
    public CancelRequestRq getCancelRequestRq() {
        return cancelRequestRq;
    }

    @JsonProperty("CancelRequestRq")
    public void setCancelRequestRq(CancelRequestRq cancelRequestRq) {
        this.cancelRequestRq = cancelRequestRq;
    }

    @JsonProperty("CancelRequestRs")
    public CreateRequestRs getCancelRequestRs() {
        return cancelRequestRs;
    }

    @JsonProperty("CancelRequestRs")
    public void setCancelRequestRs(CreateRequestRs cancelRequestRs) {
        this.cancelRequestRs = cancelRequestRs;
    }

    @JsonProperty("NotifyRequestStatusRq")
    public NotifyRequestStatusRq getNotifyRequestStatusRq() {
        return notifyRequestStatusRq;
    }

    @JsonProperty("NotifyRequestStatusRq")
    public void setNotifyRequestStatusRq(NotifyRequestStatusRq notifyRequestStatusRq) {
        this.notifyRequestStatusRq = notifyRequestStatusRq;
    }

    @JsonProperty("NotifyRequestStatusRs")
    public CreateRequestRs getNotifyRequestStatusRs() {
        return notifyRequestStatusRs;
    }

    @JsonProperty("NotifyRequestStatusRs")
    public void setNotifyRequestStatusRs(CreateRequestRs notifyRequestStatusRs) {
        this.notifyRequestStatusRs = notifyRequestStatusRs;
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
        return new HashCodeBuilder().append(createRequestRq).append(createRequestRs).append(cancelRequestRq).append(cancelRequestRs).append(notifyRequestStatusRq).append(notifyRequestStatusRs).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Requests) == false) {
            return false;
        }
        Requests rhs = ((Requests) other);
        return new EqualsBuilder().append(createRequestRq, rhs.createRequestRq).append(createRequestRs, rhs.createRequestRs).append(cancelRequestRq, rhs.cancelRequestRq).append(cancelRequestRs, rhs.cancelRequestRs).append(notifyRequestStatusRq, rhs.notifyRequestStatusRq).append(notifyRequestStatusRs, rhs.notifyRequestStatusRs).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
