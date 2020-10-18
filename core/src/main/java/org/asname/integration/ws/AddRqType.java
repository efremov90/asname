
package org.asname.integration.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AddRqType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddRqType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Param1" type="{http://anil.hcl.com/}Integer"/&gt;
 *         &lt;element name="Param2" type="{http://anil.hcl.com/}Integer"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddRqType", propOrder = {
    "param1",
    "param2"
})
public class AddRqType {

    @XmlElement(name = "Param1")
    protected int param1;
    @XmlElement(name = "Param2")
    protected int param2;

    /**
     * Gets the value of the param1 property.
     * 
     */
    public int getParam1() {
        return param1;
    }

    /**
     * Sets the value of the param1 property.
     * 
     */
    public void setParam1(int value) {
        this.param1 = value;
    }

    /**
     * Gets the value of the param2 property.
     * 
     */
    public int getParam2() {
        return param2;
    }

    /**
     * Sets the value of the param2 property.
     * 
     */
    public void setParam2(int value) {
        this.param2 = value;
    }

}
