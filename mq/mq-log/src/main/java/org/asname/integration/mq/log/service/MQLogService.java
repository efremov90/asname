package org.asname.integration.mq.log.service;

import org.asname.db.connection.MySQLConnection;
import org.asname.integration.mq.log.dao.MQLogDAO;
import org.asname.integration.mq.log.model.MQLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MQLogService {

    private Connection conn;
    private Logger logger = Logger.getLogger(MQLogService.class.getName());

    public MQLogService() {
        logger.info("start");

        conn = MySQLConnection.getConnection();

    }

    public void validateExistsMessage(String rqUID) throws Exception {
        logger.info("start");

        if (getMessageIdByUUID(rqUID) != null)
            throw new Exception(String.format("Уже есть сообщение с RqUID %s.",
                    rqUID));

    }

    public Integer create(MQLog mqLog) throws Exception {
        logger.info("start");

        Integer result = null;

        result = new MQLogDAO().create(mqLog);

        return result;
    }

    public Integer getMessageIdByUUID(String rqUID) throws SQLException {
        logger.info("start");

        Integer id = null;

        String sql = "SELECT ID  " +
                "FROM MQLOG r " +
                "WHERE RQ_UID = ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, rqUID);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            id = rs.getInt("id");
        }

        return id;
    }

}
