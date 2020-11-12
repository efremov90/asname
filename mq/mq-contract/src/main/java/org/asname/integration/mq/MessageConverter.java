package org.asname.integration.mq;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.logging.Logger;

public class MessageConverter {

    private Object obj;
    private JAXBContext jaxbContext;
    private Marshaller jaxbMarshaller;
    private Unmarshaller jaxbUnmarshaller;

    private Logger logger = Logger.getLogger(MessageConverter.class.getName());

    public MessageConverter(Object obj, String pathSchema) throws JAXBException, SAXException, MalformedURLException {
        logger.info("start");
        System.out.println(getClass().getResource(pathSchema));

        this.obj = obj;
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(getClass().getResource(pathSchema));
        jaxbContext = JAXBContext.newInstance(this.obj.getClass());
        jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setSchema(schema);
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        jaxbUnmarshaller.setSchema(schema);
    }

    public String marshal(Object obj) throws JAXBException {
        OutputStream stream = new ByteArrayOutputStream();
//        try {
            jaxbMarshaller.marshal(obj, stream);
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
        return stream.toString();
    }

    public Object unmarshal(String objectAsString) throws JAXBException {
//        try {
            return (Object) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(objectAsString.getBytes()));
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//        return null;
    }
}
