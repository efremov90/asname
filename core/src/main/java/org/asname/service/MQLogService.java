package org.asname.service;

import org.asname.dao.WSLogDAO;
import org.asname.dbConnection.MySQLConnection;
import org.asname.model.WSLog;
import org.asname.model.IntegrationMethodType;

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
import java.sql.Connection;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class MQLogService {

    private Connection conn;
    private Logger logger = Logger.getLogger(MQLogService.class.getName());

    public MQLogService() {
        logger.info("start");

        conn = MySQLConnection.getConnection();
    }

    public Integer create(WSLog wsLog) throws Exception {
        logger.info("start");

        Integer result = null;

        result = new WSLogDAO().create(wsLog);

        return result;
    }

}
