package org.asname.dao.integration;

import org.asname.dbConnection.MySQLConnection;
import org.asname.model.integration.MQLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

public class MQLogDAO {

    private Connection conn;
    private Logger logger = Logger.getLogger(MQLogDAO.class.getName());

    public MQLogDAO() {
        logger.info("start");

        conn = MySQLConnection.getConnection();
    }

    public Integer create(MQLog mqLog) throws SQLException {

        Integer result = null;

        String sql = "INSERT MQLOG (RQ_UID, CORRELATION_UID, CREATE_DATETIME, DIRECTION, CONTENT, METHOD, " +
                "STATUS, DESTINATION, ERROR) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, mqLog.getRqUID());
        st.setString(2, mqLog.getCorrelationUID());
        st.setString(3,
                new Timestamp(mqLog.getCreateDatetime().getTime()).toString());
        st.setString(4, mqLog.getDirection().name());
        st.setString(5, mqLog.getContent());
        st.setString(6, mqLog.getMethod());
        st.setString(7, mqLog.getStatus().name());
        st.setString(8, mqLog.getDestination());
        st.setString(9, mqLog.getError());
        st.executeUpdate();

        result = MySQLConnection.getLastInsertId();

        logger.info(":" + result);
        return result;
    }
}
