package org.asname.service.requests;

import org.asname.dbConnection.MySQLConnection;
import org.asname.model.requests.RequestStatusHistory;
import org.asname.model.requests.RequestStatusType;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class RequestStatusHistoryService {

    private Connection conn;
    private Logger logger = Logger.getLogger(RequestStatusHistoryService.class.getName());

    public RequestStatusHistoryService() {
        logger.info("start");

        conn = MySQLConnection.getConnection();
    }

    public ArrayList<RequestStatusHistory> getRequestStatusesHistory(int requestId) throws SQLException {
        logger.info("start");

        ArrayList<RequestStatusHistory> requestStatusesHistory = new ArrayList<>();
//        try {
        String sql = "SELECT " +
                "rsh.*, concat(ua.FIRST_NAME,IF(ua.LAST_NAME IS NULL,'',CONCAT(' ',ua.LAST_NAME))) user_name " +
                "FROM REQUEST_STATUS_HISTORY rsh " +
                "INNER JOIN USER_ACCOUNT ua ON rsh.USER_ACCOUNT_ID = ua.ID " +
                "WHERE rsh.REQUEST_ID = " + requestId;

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
            requestStatusHistory.setId(rs.getInt("id"));
            requestStatusHistory.setRequestId(rs.getInt("request_id"));
            requestStatusHistory.setStatus(RequestStatusType.valueOf(rs.getNString("status")));
            requestStatusHistory.setComment(rs.getNString("comment"));
            requestStatusHistory.setLastStatus(rs.getInt("IS_LAST_STATUS") == 1 ? true : false);
            requestStatusHistory.setEventDateTime(Timestamp.valueOf(rs.getNString("event_datetime")));
            requestStatusHistory.setUserId(rs.getInt("user_account_id"));
            requestStatusHistory.setUserName(rs.getNString("user_name"));
            requestStatusesHistory.add(requestStatusHistory);
        }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return requestStatusesHistory;
    }
}
