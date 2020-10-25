package org.asname.dao;

import org.asname.dbConnection.MySQLConnection;
import org.asname.model.WSLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

public class WSLogDAO {

    private Connection conn;
    private Logger logger = Logger.getLogger(WSLogDAO.class.getName());

    public WSLogDAO() {
        logger.info("start");

        conn = MySQLConnection.getConnection();
    }

    public Integer create(WSLog wsLog) throws SQLException {

        Integer result = null;

        String sql = "INSERT WSLOG (DIRECTION, REQUEST, RESPONSE, METHOD, STATUS, START_DATETIME, " +
                "END_DATETIME) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, wsLog.getDirection().name());
        st.setString(2, wsLog.getRequest());
        st.setString(3, wsLog.getResponse());
        st.setString(4, wsLog.getMethod());
        st.setString(5, wsLog.getStatus().name());
        st.setString(6, new Timestamp(wsLog.getStartDatetime().getTime()).toString());
        st.setString(7, new Timestamp(wsLog.getEndDatetime().getTime()).toString());
        st.executeUpdate();

        result = MySQLConnection.getLastInsertId();

        logger.info(":" + result);
        return result;
    }
}
