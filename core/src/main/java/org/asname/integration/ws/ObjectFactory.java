
package org.asname.integration.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.asname.integration.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AddRq_QNAME = new QName("http://anil.hcl.com/", "AddRq");
    private final static QName _AddRs_QNAME = new QName("http://anil.hcl.com/", "AddRs");
    private final static QName _DiffRq_QNAME = new QName("http://anil.hcl.com/", "DiffRq");
    private final static QName _DiffRs_QNAME = new QName("http://anil.hcl.com/", "DiffRs");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.asname.integration.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddRqType }
     * 
     */
    public AddRqType createAddRqType() {
        return new AddRqType();
    }

    /**
     * Create an instance of {@link ResultType }
     * 
     */
    public ResultType createResultType() {
        return new ResultType();
    }

    /**
     * Create an instance of {@link DiffRqType }
     * 
     */
    public DiffRqType createDiffRqType() {
        return new DiffRqType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRqType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AddRqType }{@code >}
     */
    @XmlElementDecl(namespace = "http://anil.hcl.com/", name = "AddRq")
    public JAXBElement<AddRqType> createAddRq(AddRqType value) {
        return new JAXBElement<AddRqType>(_AddRq_QNAME, AddRqType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ResultType }{@code >}
     */
    @XmlElementDecl(namespace = "http://anil.hcl.com/", name = "AddRs")
    public JAXBElement<ResultType> createAddRs(ResultType value) {
        return new JAXBElement<ResultType>(_AddRs_QNAME, ResultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DiffRqType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DiffRqType }{@code >}
     */
    @XmlElementDecl(namespace = "http://anil.hcl.com/", name = "DiffRq")
    public JAXBElement<DiffRqType> createDiffRq(DiffRqType value) {
        return new JAXBElement<DiffRqType>(_DiffRq_QNAME, DiffRqType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ResultType }{@code >}
     */
    @XmlElementDecl(namespace = "http://anil.hcl.com/", name = "DiffRs")
    public JAXBElement<ResultType> createDiffRs(ResultType value) {
        return new JAXBElement<ResultType>(_DiffRs_QNAME, ResultType.class, null, value);
    }

}
