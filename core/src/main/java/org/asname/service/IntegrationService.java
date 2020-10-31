package org.asname.service;

import org.asname.model.IntegrationMethodType;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Arrays;
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

    public IntegrationMethodType getMethod(String req) {
        IntegrationMethodType result = IntegrationMethodType.Unknown;
        for (IntegrationMethodType value : IntegrationMethodType.values()) {
            if (!value.equals(IntegrationMethodType.Unknown)) {
                final Pattern PATTERN = Pattern.compile(value.name().toLowerCase());
                if (PATTERN.matcher(req.toString().toLowerCase()).find()) {
                    return value;
                }
            }
        }
        return result;
    }
}
