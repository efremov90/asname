
package org.asname.integration.wsspring.wsone;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.asname.integration.wsspring package. 
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

    private final static QName _AddRequest_QNAME = new QName("http://anil.hcl.com/calWebService/schemas", "AddRequest");
    private final static QName _DiffResponse_QNAME = new QName("http://anil.hcl.com/calWebService/schemas", "DiffResponse");
    private final static QName _AddResponse_QNAME = new QName("http://anil.hcl.com/calWebService/schemas", "AddResponse");
    private final static QName _DiffRequest_QNAME = new QName("http://anil.hcl.com/calWebService/schemas", "DiffRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.asname.integration.wsspring
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
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRqType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://anil.hcl.com/calWebService/schemas", name = "AddRequest")
    public JAXBElement<AddRqType> createAddRequest(AddRqType value) {
        return new JAXBElement<AddRqType>(_AddRequest_QNAME, AddRqType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://anil.hcl.com/calWebService/schemas", name = "DiffResponse")
    public JAXBElement<ResultType> createDiffResponse(ResultType value) {
        return new JAXBElement<ResultType>(_DiffResponse_QNAME, ResultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://anil.hcl.com/calWebService/schemas", name = "AddResponse")
    public JAXBElement<ResultType> createAddResponse(ResultType value) {
        return new JAXBElement<ResultType>(_AddResponse_QNAME, ResultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DiffRqType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://anil.hcl.com/calWebService/schemas", name = "DiffRequest")
    public JAXBElement<DiffRqType> createDiffRequest(DiffRqType value) {
        return new JAXBElement<DiffRqType>(_DiffRequest_QNAME, DiffRqType.class, null, value);
    }

}
