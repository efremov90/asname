package org.asname.service.audit;

import org.asname.dbConnection.MySQLConnection;
import org.asname.model.audit.AuditOper;
import org.asname.model.audit.AuditOperType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class AuditOperService {

    private Connection conn;
    private Logger logger = Logger.getLogger(AuditOperService.class.getName());

    public AuditOperService() throws ClassNotFoundException {
        logger.info("start");

        conn = MySQLConnection.getConnection();
    }

    public AuditOper getAuditOperByCode(AuditOperType type) throws SQLException {
        logger.info("start");

        AuditOper auditOper = new AuditOper();
//        try {
        String sql = "SELECT " +
                "* " +
                "FROM AUDIT_OPER " +
                "WHERE TYPE = ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, type.name());
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            auditOper.setId(rs.getInt("Id"));
            auditOper.setType(rs.getNString("Type"));
            auditOper.setDescription(rs.getNString("Description"));
        }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return auditOper;
    }
}
