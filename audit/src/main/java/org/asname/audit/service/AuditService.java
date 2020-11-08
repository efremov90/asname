package org.asname.audit.service;

import org.asname.audit.dao.AuditDAO;
import org.asname.audit.model.Audit;
import org.asname.audit.model.AuditOperType;
import org.asname.db.connection.MySQLConnection;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

public class AuditService {

    private Connection conn;
    private Logger logger = Logger.getLogger(AuditService.class.getName());

    public AuditService() {
        logger.info("start");

        conn = MySQLConnection.getConnection();
    }

    private String getDescription(AuditOperType type, Object[] args) {
        return new MessageFormat(type.getDescription()).format(args);
    }

    public Integer create(AuditOperType type, int userAccountId, Date eventDatetime, String content, Object... args) throws Exception {
        logger.info("start");

        Integer result = null;

//        try {
        Audit audit = new Audit();
        audit.setAuditOperId(new AuditOperService().getAuditOperByCode(type).getId());
        audit.setUserAccountId(userAccountId);
        audit.setEventDateTime(eventDatetime);
        audit.setDescription(getDescription(type, args));
        audit.setContent(content);

        result = new AuditDAO().create(audit);
/*        } catch (Exception e) {
            result=false;
            e.printStackTrace();
        }*/

        return result;
    }

    public ArrayList<Audit> getAudits(String where) throws SQLException {
        logger.info("start");

        ArrayList<Audit> audits = new ArrayList<>();

        String sql = "SELECT " +
                "a.ID, ao.TYPE, concat(ua.FIRST_NAME,IF(ua.LAST_NAME IS NULL,'',CONCAT(' ',ua.LAST_NAME))) user_name, " +
                "a.EVENT_DATETIME, a.DESCRIPTION, a.CONTENT " +
                "FROM AUDIT a " +
                "INNER JOIN AUDIT_OPER ao ON a.AUDIT_OPER_ID = ao.id " +
                "INNER JOIN USER_ACCOUNT ua ON a.USER_ACCOUNT_ID = ua.ID " +
                where;

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            Audit audit = new Audit();
            audit.setId(rs.getInt("id"));
            audit.setAuditOperType(AuditOperType.valueOf(rs.getNString("TYPE")));
            audit.setUserName(rs.getNString("user_name"));
            audit.setEventDateTime(Timestamp.valueOf(rs.getNString("EVENT_DATETIME")));
            audit.setDescription(rs.getNString("DESCRIPTION"));
            audit.setContent(rs.getNString("CONTENT"));
            audits.add(audit);
        }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return audits;
    }
}
