
package org.asname.integration.contract.requests.mq;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for NotifyRequestStatusRqType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NotifyRequestStatusRqType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://org.asname.requests/schemas}BaseType">
 *       &lt;sequence>
 *         &lt;element name="NotifyRequestStatusRequest" type="{http://org.asname.requests/schemas}NotifyRequestStatusRequestType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "NotifyRequestStatusRq",namespace = "http://org.asname.requests/schemas")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotifyRequestStatusRqType", namespace = "http://org.asname.requests/schemas", propOrder = {
    "notifyRequestStatusRequest"
})
public class NotifyRequestStatusRqType
    extends BaseType
{

    @XmlElement(name = "NotifyRequestStatusRequest", required = true)
    protected NotifyRequestStatusRequestType notifyRequestStatusRequest;

    /**
     * Gets the value of the notifyRequestStatusRequest property.
     * 
     * @return
     *     possible object is
     *     {@link NotifyRequestStatusRequestType }
     *     
     */
    public NotifyRequestStatusRequestType getNotifyRequestStatusRequest() {
        return notifyRequestStatusRequest;
    }

    /**
     * Sets the value of the notifyRequestStatusRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link NotifyRequestStatusRequestType }
     *     
     */
    public void setNotifyRequestStatusRequest(NotifyRequestStatusRequestType value) {
        this.notifyRequestStatusRequest = value;
    }

}
