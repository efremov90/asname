
package org.asname.integration.contract.requests.mq;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for CreateRequestRqType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateRequestRqType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://org.asname.requests/schemas}BaseType">
 *       &lt;sequence>
 *         &lt;element name="CreateRequest" type="{http://org.asname.requests/schemas}CreateRequestType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "CreateRequestRq",namespace = "http://org.asname.requests/schemas")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateRequestRqType", namespace = "http://org.asname.requests/schemas", propOrder = {
    "createRequest"
})
public class CreateRequestRqType
    extends BaseType
{

    @XmlElement(name = "CreateRequest", required = true)
    protected CreateRequestType createRequest;

    /**
     * Gets the value of the createRequest property.
     * 
     * @return
     *     possible object is
     *     {@link CreateRequestType }
     *     
     */
    public CreateRequestType getCreateRequest() {
        return createRequest;
    }

    /**
     * Sets the value of the createRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateRequestType }
     *     
     */
    public void setCreateRequest(CreateRequestType value) {
        this.createRequest = value;
    }

}
