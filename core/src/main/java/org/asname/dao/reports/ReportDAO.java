package org.asname.dao.reports;

import org.asname.dbConnection.MySQLConnection;
import org.asname.model.reports.FormatReportType;
import org.asname.model.reports.Report;
import org.asname.model.reports.ReportStatusType;
import org.asname.model.reports.ReportType;

import java.sql.*;
import java.util.logging.Logger;

import static org.asname.model.reports.ReportStatusType.CREATED;

public class ReportDAO {

    private Connection conn;
    private Logger logger = Logger.getLogger(ReportDAO.class.getName());

    public ReportDAO() {
        logger.info("start");

        conn = MySQLConnection.getConnection();
    }

    public Integer create(Report report) throws SQLException {
        logger.info("start");

        Integer result = null;
//        try {
        String sql = "INSERT REPORTS (TYPE, STATUS, COMMENT, CONTENT, PARAMETERS, FROM_PERIOD_DATE, TO_PERIOD_DATE, " +
                "FORMAT) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?); ";
        PreparedStatement st = conn.prepareStatement(sql);

        st.setString(1, report.getType().name());
        st.setString(2, CREATED.name());
        st.setString(3, null);
        st.setString(4, null);
        st.setString(5, report.getParameters());
        st.setDate(6, report.getFromPeriodDate() != null ? new Date(report.getFromPeriodDate().getTime()) : null);
        st.setDate(7, report.getToPeriodDate() != null ? new Date(report.getToPeriodDate().getTime()) : null);
        st.setString(8, report.getFormat().name());
        st.executeUpdate();

        result = MySQLConnection.getLastInsertId();
/*        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        logger.info(":" + result);
        return result;
    }

    public ReportType getType(int reportId) throws SQLException {
        logger.info("start");

        ReportType result = null;
//        try {
        String sql = "SELECT TYPE  " +
                "FROM REPORTS r " +
                "WHERE ID = ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, reportId);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            result = ReportType.valueOf(rs.getString("TYPE"));
        }
/*        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        logger.info(":" + result);
        return result;
    }

    public FormatReportType getFormat(int reportId) throws SQLException {
        logger.info("start");

        FormatReportType result = null;
//        try {
        String sql = "SELECT FORMAT  " +
                "FROM REPORTS r " +
                "WHERE ID = ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, reportId);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            result = FormatReportType.valueOf(rs.getString("FORMAT"));
        }
/*        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        logger.info(":" + result);
        return result;
    }

    public ReportStatusType getReportStatus(int reportId) throws SQLException {
        logger.info("start");

        ReportStatusType reportStatusType = null;
//        try {
        String sql = "SELECT STATUS  " +
                "FROM REPORTS r " +
                "WHERE ID = ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, reportId);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            reportStatusType = ReportStatusType.valueOf(rs.getString("STATUS"));
        }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return reportStatusType;
    }

    public String getParameters(int reportId) throws SQLException {
        logger.info("start");

        String result = null;
//        try {
        String sql = "SELECT PARAMETERS  " +
                "FROM REPORTS r " +
                "WHERE ID = ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, reportId);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            result = rs.getString("PARAMETERS");
        }
/*        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        logger.info(":" + result);
        return result;
    }
}
