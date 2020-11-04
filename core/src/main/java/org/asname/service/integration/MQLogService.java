package org.asname.service.integration;

import org.asname.dao.integration.MQLogDAO;
import org.asname.dbConnection.MySQLConnection;
import org.asname.model.integration.MQLog;

import java.sql.Connection;
import java.util.logging.Logger;

public class MQLogService {

    private Connection conn;
    private Logger logger = Logger.getLogger(MQLogService.class.getName());

    public MQLogService() {
        logger.info("start");

        conn = MySQLConnection.getConnection();
    }

    public Integer create(MQLog mqLog) throws Exception {
        logger.info("start");

        Integer result = null;

        result = new MQLogDAO().create(mqLog);

        return result;
    }

}
