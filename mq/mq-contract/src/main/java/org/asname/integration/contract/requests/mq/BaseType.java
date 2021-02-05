
package org.asname.integration.contract.requests.mq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for BaseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RqUID" type="{http://org.asname.requests/schemas}UUID36Type"/>
 *         &lt;element name="RqTm" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="CorrelationUID" type="{http://org.asname.requests/schemas}UUID36Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseType", namespace = "http://org.asname.requests/schemas", propOrder = {
    "rqUID",
    "rqTm",
    "correlationUID"
})
@XmlSeeAlso({
    ResultType.class,
    CreateRequestRqType.class,
    NotifyRequestStatusRqType.class,
    CancelRequestRqType.class
})
public abstract class BaseType {

    @XmlElement(name = "RqUID", required = true)
    protected String rqUID;
    @XmlElement(name = "RqTm", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar rqTm;
    @XmlElement(name = "CorrelationUID")
    protected String correlationUID;

    /**
     * Gets the value of the rqUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRqUID() {
        return rqUID;
    }

    /**
     * Sets the value of the rqUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRqUID(String value) {
        this.rqUID = value;
    }

    /**
     * Gets the value of the rqTm property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRqTm() {
        return rqTm;
    }

    /**
     * Sets the value of the rqTm property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRqTm(XMLGregorianCalendar value) {
        this.rqTm = value;
    }

    /**
     * Gets the value of the correlationUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorrelationUID() {
        return correlationUID;
    }

    /**
     * Sets the value of the correlationUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorrelationUID(String value) {
        this.correlationUID = value;
    }

}
