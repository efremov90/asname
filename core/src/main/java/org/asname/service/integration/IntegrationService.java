package org.asname.service.integration;

import org.asname.model.integration.MethodType;

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

    public MethodType getMethod(String req) {
        MethodType result = MethodType.Unknown;
        for (MethodType value : MethodType.values()) {
            if (!value.equals(MethodType.Unknown)) {
                final Pattern PATTERN = Pattern.compile(value.name().toLowerCase());
                if (PATTERN.matcher(req.toString().toLowerCase()).find()) {
                    return value;
                }
            }
        }
        return result;
    }
}
