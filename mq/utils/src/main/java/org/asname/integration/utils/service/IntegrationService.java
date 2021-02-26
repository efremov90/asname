package org.asname.integration.utils.service;

import org.asname.integration.utils.model.MethodType;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IntegrationService {

    public String getExceptionString(Exception e) throws IOException {
        return e.toString() + "\n" +
                Arrays.stream(e.getStackTrace()).map(x -> x.toString()).collect(Collectors.joining("\n"));
    }

    public String transformXML(String xml) throws XMLStreamException, TransformerException
    {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        Writer out = new StringWriter();
        t.transform(new StreamSource(new StringReader(xml)), new StreamResult(out));
        return out.toString();
    }

    public MethodType getMethod(String message) {
        MethodType result = null;
        String beginMessage = (message != null ? message.substring(0,Math.min(400,message.length())) : null);
        for (MethodType value : MethodType.values()) {
//            if (!value.equals(MethodType.Unknown)) {
                final Pattern PATTERN = Pattern.compile(value.name().toLowerCase());
                if (PATTERN.matcher(beginMessage.toString().toLowerCase()).find()) {
                    return value;
                }
//            }
        }
        if (result==null) result=MethodType.Unknown;
        return result;
    }

    public XMLGregorianCalendar getXMLGregorianCalendar(Date date) throws DatatypeConfigurationException {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
    }
}
