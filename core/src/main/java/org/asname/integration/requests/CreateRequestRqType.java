
package org.asname.integration.requests;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for CreateRequestRqType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateRequestRqType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RequestUUID" type="{http://org.asname.requests/schemas}UUID36Type"/>
 *         &lt;element name="CreateDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="ClientCode" type="{http://org.asname.requests/schemas}ClientCodeType"/>
 *         &lt;element name="Comment" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "CreateRequestRq", namespace = "http://org.asname.requests/schemas")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateRequestRqType", namespace = "http://org.asname.requests/schemas", propOrder = {
    "requestUUID",
    "createDate",
    "clientCode",
    "comment"
})
public class CreateRequestRqType {

    @XmlElement(name = "RequestUUID", required = true)
    protected String requestUUID;
    @XmlElement(name = "CreateDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar createDate;
    @XmlElement(name = "ClientCode", required = true)
    protected String clientCode;
    @XmlElement(name = "Comment", required = true)
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
     * Gets the value of the createDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreateDate() {
        return createDate;
    }

    /**
     * Sets the value of the createDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreateDate(XMLGregorianCalendar value) {
        this.createDate = value;
    }

    /**
     * Gets the value of the clientCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientCode() {
        return clientCode;
    }

    /**
     * Sets the value of the clientCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientCode(String value) {
        this.clientCode = value;
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
        this.comment = value;
    }

}
