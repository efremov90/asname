
package org.asname.integration.contract.requests.mq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NotifyRequestStatusRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NotifyRequestStatusRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RequestUUID" type="{http://org.asname.requests/schemas}UUID36Type"/>
 *         &lt;element name="Status" type="{http://org.asname.requests/schemas}RequestStatusType"/>
 *         &lt;element name="Comment" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotifyRequestStatusRequestType", namespace = "http://org.asname.requests/schemas", propOrder = {
    "requestUUID",
    "status",
    "comment"
})
public class NotifyRequestStatusRequestType {

    @XmlElement(name = "RequestUUID", required = true)
    protected String requestUUID;
    @XmlElement(name = "Status", required = true)
    protected String status;
    @XmlElement(name = "Comment")
    protected String comment;

    /**
     * Gets the value of the requestUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestUUID() {
        return requestUUID;
    }

    /**
     * Sets the value of the requestUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestUUID(String value) {
        this.requestUUID = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = (value != null ? value.substring(0,Math.min(value.length(),2000)) : null);
    }

}
