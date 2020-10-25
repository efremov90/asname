package org.asname.service;

import org.asname.dao.WSLogDAO;
import org.asname.dbConnection.MySQLConnection;
import org.asname.model.WSLog;

import java.sql.*;
import java.util.logging.Logger;

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

}
