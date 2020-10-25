package org.asname.service;

import org.asname.dao.WSLogDAO;
import org.asname.dbConnection.MySQLConnection;
import org.asname.model.WSLog;
import org.asname.model.WSMethodType;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class WSLogService {

    private Connection conn;
    private Logger logger = Logger.getLogger(WSLogService.class.getName());

    public WSLogService() {
        logger.info("start");

        conn = MySQLConnection.getConnection();
    }

    public Integer create(WSLog wsLog) throws Exception {
        logger.info("start");

        Integer result = null;

        result = new WSLogDAO().create(wsLog);

        return result;
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

    public WSMethodType getMethod(String req) {
        WSMethodType result = WSMethodType.Unknown;
        for (WSMethodType value : WSMethodType.values()) {
            if (!value.equals(WSMethodType.Unknown)) {
                final Pattern PATTERN = Pattern.compile(value.name().toLowerCase());
                if (PATTERN.matcher(req.toString().toLowerCase()).find()) {
                    return value;
                }
            }
        }
        return result;
    }

}
