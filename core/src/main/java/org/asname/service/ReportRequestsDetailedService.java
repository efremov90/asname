package org.asname.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.dao.UserAccountDAO;
import org.asname.dbConnection.MySQLConnection;
import org.asname.dto.ReportRequestsDetailedDTO;
import org.asname.model.*;
import org.asname.reportbean.ReportRequestsDetailedBean;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static org.asname.model.Permissions.*;

public class ReportRequestsDetailedService extends ReportService {

    private Connection conn;
    private Logger logger = Logger.getLogger(ReportRequestsDetailedService.class.getName());

    public ReportRequestsDetailedService() {
        logger.info("start");

        conn = MySQLConnection.getConnection();
    }

    public Map<String, Object> getParametersJR(ReportRequestsDetailedDTO parameters) {
        Map<String, Object> parametersJR = new HashMap<String, Object>();
        parametersJR.put("fromCreateDate",
                (parameters.getFromCreateDate() != null && parameters.getFromCreateDate() != "") ?
                        java.sql.Date.valueOf(parameters.getFromCreateDate()) : "");
        parametersJR.put("toCreateDate",
                (parameters.getToCreateDate() != null && parameters.getToCreateDate() != "") ?
                        java.sql.Date.valueOf(parameters.getToCreateDate()) : "");
        return parametersJR;
    }

    public Integer create(ReportRequestsDetailedDTO parameters, FormatReportType formatReportType, int userAccountId) throws Exception {
        logger.info("start");

        Integer result = null;

        UserAccount userAccount = new UserAccountDAO().getUserAccountById(userAccountId);
        if (!new PermissionService().isPermission(userAccountId, REPORT_GENERATE_REQUESTS_DETAILED))
            throw new Exception(String.format("У пользователя %s отсутствует разрешение %s.",
                    userAccount.getAccount(),
                    REPORT_GENERATE_REQUESTS_DETAILED.name()));

        Report report = new Report();
        report.setType(ReportType.REPORT_REQUESTS_DETAILED);
        report.setFormat(formatReportType);
        report.setFromPeriodDate((parameters.getFromCreateDate() != null && parameters.getFromCreateDate() != "") ?
                java.sql.Date.valueOf(parameters.getFromCreateDate()) : null);
        report.setToPeriodDate((parameters.getToCreateDate() != null && parameters.getToCreateDate() != "") ?
                java.sql.Date.valueOf(parameters.getToCreateDate()) : null);
        report.setParameters(new ObjectMapper().writeValueAsString(parameters));
        result = super.create(report, userAccountId);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new ReportTaskService(result));
        executorService.shutdown();

        logger.info(":" + result);
        return result;
    }

    public ArrayList<ReportRequestsDetailedBean> getData(Date fromCreateDate, Date toCreateDate, String clientCode) throws Exception {
        logger.info("start");

        ArrayList<ReportRequestsDetailedBean> result = new ArrayList<>();

        String sql = "SELECT r.client_code, c.client_name, ct.type, r.create_date, r.status, r.comment " +
                "FROM REQUESTS r " +
                "LEFT JOIN CLIENTS c on r.client_code = c.client_code " +
                "LEFT JOIN client_type ct on c.client_type_id = ct.id " +
                "WHERE 1=1 " +
                (fromCreateDate != null ? " AND r.CREATE_DATE >= " + "'" + fromCreateDate + "'" : "") +
                (toCreateDate != null ? " AND r.CREATE_DATE <= " + "'" + toCreateDate + "'" : "") +
                (clientCode != null ? " AND r.CLIENT_CODE = " + "'" + clientCode + "'" : "") +
                "ORDER BY ct.type, c.client_name, c.client_code, r.create_date, r.id";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            ReportRequestsDetailedBean reportRequestsDetailedBean = new ReportRequestsDetailedBean();
            reportRequestsDetailedBean.setClientCode(rs.getNString("client_code"));
            reportRequestsDetailedBean.setClientName(rs.getNString("client_name"));
            reportRequestsDetailedBean.setClientType(ClientTypeType.valueOf(rs.getNString("type")).getDescription());
            reportRequestsDetailedBean.setCreateDate(new java.sql.Date(rs.getDate("create_date").getTime()).toString());
            reportRequestsDetailedBean.setStatus(RequestStatusType.valueOf(rs.getNString("status")).getDescription());
            reportRequestsDetailedBean.setComment(rs.getNString("comment"));
            reportRequestsDetailedBean.setRankSorted(0);
            result.add(reportRequestsDetailedBean);
        }

//        Thread.sleep(10000);

        logger.info("finish");
        return result;
    }

}
