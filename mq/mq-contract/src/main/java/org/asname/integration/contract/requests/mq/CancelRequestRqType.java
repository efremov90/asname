
package org.asname.integration.contract.requests.mq;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for CancelRequestRqType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CancelRequestRqType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://org.asname.requests/schemas}BaseType">
 *       &lt;sequence>
 *         &lt;element name="CancelRequest" type="{http://org.asname.requests/schemas}CancelRequestType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "CancelRequestRq",namespace = "http://org.asname.requests/schemas")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CancelRequestRqType", namespace = "http://org.asname.requests/schemas", propOrder = {
    "cancelRequest"
})
public class CancelRequestRqType
    extends BaseType
{

    @XmlElement(name = "CancelRequest", required = true)
    protected CancelRequestType cancelRequest;

    /**
     * Gets the value of the cancelRequest property.
     * 
     * @return
     *     possible object is
     *     {@link CancelRequestType }
     *     
     */
    public CancelRequestType getCancelRequest() {
        return cancelRequest;
    }

    /**
     * Sets the value of the cancelRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link CancelRequestType }
     *     
     */
    public void setCancelRequest(CancelRequestType value) {
        this.cancelRequest = value;
    }

}
