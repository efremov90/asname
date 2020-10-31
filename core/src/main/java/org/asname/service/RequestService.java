package org.asname.service;

import org.asname.dao.*;
import org.asname.dbConnection.MySQLConnection;
import org.asname.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.asname.model.Configures.CANCEL_REQUEST_INTERVAL;
import static org.asname.model.Permissions.*;
import static org.asname.model.RequestStatusType.*;
import static org.asname.model.TaskType.CANCEL_REQUEST;

public class RequestService {

    private Connection conn;
    private Logger logger = Logger.getLogger(RequestService.class.getName());

    public RequestService() {
        logger.info("start");

        conn = MySQLConnection.getConnection();
    }

    public void validateExistsRequest(String requestUUID) throws Exception {
        logger.info("start");
//        try {
        if (getRequestIdByUUID(requestUUID) != null)
            throw new Exception(String.format("Уже есть заявка с UUID %s.",
                    requestUUID));
/*        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public Integer getRequestIdByUUID(String requestUUID) throws SQLException {
        logger.info("start");

        Integer id = null;
//        try {
        String sql = "SELECT ID  " +
                "FROM REQUESTS r " +
                "WHERE REQUEST_UUID = ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, requestUUID);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            id = rs.getInt("id");
        }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return id;
    }

    public Request getRequestById(int id) throws SQLException {
        return getRequest(id, null);
    }

    public Request getRequestByUUID(String requestUUID) throws SQLException {
        return getRequest(-1, requestUUID);
    }

    private Request getRequest(int requestId, String requestUUID) throws SQLException {
        logger.info("start");

        Request request = null;
//        try {
        String sql = "SELECT " +
                "r.ID, r.REQUEST_UUID, r.CREATE_DATE, r.CREATE_DATETIME, r.CLIENT_CODE, r.COMMENT, r. STATUS, " +
                "rsh.EVENT_DATETIME, rsh.USER_ACCOUNT_ID " +
                "FROM REQUESTS r " +
                "LEFT JOIN REQUEST_STATUS_HISTORY rsh ON r.ID = rsh.REQUEST_ID AND rsh.IS_LAST_STATUS = 1 " +
                "WHERE 1=1 " +
                (requestId != -1 ? " AND r.ID = " + requestId : "") +
                (requestUUID != null ? " AND r.REQUEST_UUID = " + "'" + requestUUID + "'" : "");

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        if (rs.next()) {
            request = new Request();
            request.setId(rs.getInt("id"));
            request.setRequestUUID(rs.getNString("request_uuid"));
            request.setCreateDate(new java.util.Date(rs.getDate("create_date").getTime()));
            request.setCreateDateTime(Timestamp.valueOf(rs.getNString("create_datetime")));
            request.setClientCode(rs.getNString("client_code"));
            request.setComment(rs.getNString("comment"));
            request.setRequestStatus(RequestStatusType.valueOf(rs.getNString("status")));
            request.setLastDateTimeChangeRequestStatus(Timestamp.valueOf(rs.getNString("event_datetime")));
            request.setLastUserAccountIdChangeRequestStatus(rs.getInt("user_account_id"));
        }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return request;
    }

    public Integer create(Request request, int userAccountId) throws Exception {
        logger.info("start");

        Integer result = null;
//        try {
        UserAccount userAccount = new UserAccountDAO().getUserAccountById(userAccountId);
        if (!new PermissionService().isPermission(userAccountId, REQUESTS_CREATE))
            throw new Exception(String.format("У пользователя %s отсутствует разрешение %s.",
                    userAccount.getAccount(),
                    REQUESTS_CREATE.name()));
        if (getRequestByUUID(request.getRequestUUID()) != null)
            throw new Exception(String.format("Уже есть заявка с UUID %s.",
                    request.getRequestUUID()));
        if (request.getClientCode() == null || request.getClientCode() == "")
            throw new Exception(String.format("Не задан код клиента."));
        if (new ClientService().getClient(request.getClientCode()) == null)
            throw new Exception(String.format("Не найден клиент с кодом %s.",
                    request.getClientCode()));

        conn.setAutoCommit(false);

        request.setRequestStatus(CREATED);
        request.setLastDateTimeChangeRequestStatus(new java.util.Date());
        result = new RequestDAO().create(request);

        Integer requestId = result;

        RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
        requestStatusHistory.setRequestId(requestId);
        requestStatusHistory.setStatus(CREATED);
        requestStatusHistory.setEventDateTime(request.getLastDateTimeChangeRequestStatus());
        requestStatusHistory.setUserId(userAccountId);
        new RequestStatusHistoryDAO().create(requestStatusHistory);

        Integer auditId = new AuditService().create(
                AuditOperType.CREATE_REQUEST,
                userAccountId,
                request.getCreateDateTime(),
                String.format("UUID заявки: %s \n" +
                                "Дата создания: %s \n" +
                                "Код клиента: %s \n" +
                                "Комментарий: %s",
                        request.getRequestUUID(),
                        request.getCreateDate().toString(),
                        request.getClientCode(),
                        request.getComment()),
                requestId
        );

        new RequestAuditsDAO().create(requestId, auditId);

        if (request.getClientCode().equals("1001")) {
            Task task = new Task();
            task.setType(CANCEL_REQUEST);
            task.setCreateDateTime(new java.util.Date());
            task.setCreateDate(task.getCreateDateTime());
            task.setPlannedStartDateTime(task.getCreateDateTime());
            task.setStatus(TaskStatusType.CREATED);
            task.setUserAccountId(userAccountId);

            Integer taskId = new TaskService().create(task, userAccountId, requestId);

            new RequestTasksDAO().create(requestId, taskId);

            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.schedule(new RequestTaskService(result),
                    Integer.valueOf(CANCEL_REQUEST_INTERVAL.getValue()),
                    TimeUnit.SECONDS);
            scheduledExecutorService.shutdown();
        }

        conn.commit();
        conn.setAutoCommit(true);
/*        } catch (Exception e) {
            result=false;
            e.printStackTrace();
        }*/

        return result;
    }

    public boolean cancel(String requestUUID, int userAccountId, String comment) throws Exception {
        logger.info("start");

        boolean result = false;
//        try {
        UserAccount userAccount = new UserAccountDAO().getUserAccountById(userAccountId);
        if (!new PermissionService().isPermission(userAccountId, REQUESTS_CANCEL))
            throw new Exception(String.format("У пользователя %s отсутствует разрешение %s.",
                    userAccount.getAccount(),
                    REQUESTS_CANCEL.name()));
        if (getRequestIdByUUID(requestUUID) == null)
            throw new Exception(String.format("Заявка с UUID %s отсутствует.",
                    requestUUID));

        Request request = new RequestService().getRequestByUUID(requestUUID);

        if (getRequestByUUID(requestUUID).getRequestStatus() != CREATED)
            throw new Exception(String.format("Заявка в статусе %s.",
                    request.getRequestStatus().getDescription()));

        conn.setAutoCommit(false);
        request.setRequestStatus(CANCELED);
        request.setLastDateTimeChangeRequestStatus(new java.util.Date());
        result = new RequestDAO().edit(request);

        RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
        requestStatusHistory.setRequestId(request.getId());
        requestStatusHistory.setStatus(CANCELED);
        requestStatusHistory.setComment(comment);
        requestStatusHistory.setEventDateTime(request.getLastDateTimeChangeRequestStatus());
        requestStatusHistory.setUserId(userAccountId);
        new RequestStatusHistoryDAO().create(requestStatusHistory);

        new AuditService().create(
                AuditOperType.CANCEL_REQUEST,
                userAccountId,
                requestStatusHistory.getEventDateTime(),
                String.format("Комментарий: %s",
                        requestStatusHistory.getComment()),
                request.getId()
        );

        Integer auditId = MySQLConnection.getLastInsertId();

        new RequestAuditsDAO().create(request.getId(), auditId);

        conn.commit();
        conn.setAutoCommit(true);
        //result = new RequestDAO().create(request);
/*        } catch (Exception e) {
            result=false;
            e.printStackTrace();
        }*/

        return result;
    }

    public boolean close(String requestUUID, int userAccountId, String comment) throws Exception {
        logger.info("start");

        boolean result = false;

        UserAccount userAccount = new UserAccountDAO().getUserAccountById(userAccountId);
        if (!new PermissionService().isPermission(userAccountId, REQUESTS_CLOSE))
            throw new Exception(String.format("У пользователя %s отсутствует разрешение %s.",
                    userAccount.getAccount(),
                    REQUESTS_CLOSE.name()));
        if (getRequestIdByUUID(requestUUID) == null)
            throw new Exception(String.format("Заявка с UUID %s отсутствует.",
                    requestUUID));

        Request request = new RequestService().getRequestByUUID(requestUUID);

        conn.setAutoCommit(false);
        request.setRequestStatus(CLOSE);
        request.setComment(comment);
        request.setLastDateTimeChangeRequestStatus(new java.util.Date());
        result = new RequestDAO().edit(request);

        RequestStatusHistory requestStatusHistory = new RequestStatusHistory();
        requestStatusHistory.setRequestId(request.getId());
        requestStatusHistory.setStatus(CLOSE);
        requestStatusHistory.setEventDateTime(request.getLastDateTimeChangeRequestStatus());
        requestStatusHistory.setUserId(userAccountId);
        new RequestStatusHistoryDAO().create(requestStatusHistory);

        Integer auditId = new AuditService().create(
                AuditOperType.CLOSE_REQUEST,
                userAccountId,
                requestStatusHistory.getEventDateTime(),
                "",
                request.getId()
        );

        new RequestAuditsDAO().create(request.getId(), auditId);

        conn.commit();
        conn.setAutoCommit(true);

        return result;
    }

    public ArrayList<Request> getRequests(Date fromCreateDate, Date toCreateDate, String clientCode,
                                          RequestStatusType requestStatus) throws SQLException {
        logger.info("start");

        ArrayList<Request> requests = new ArrayList<>();
//        try {
        String sql = "SELECT " +
                "r.ID, r.REQUEST_UUID, r.CREATE_DATE, r.CREATE_DATETIME, r.CLIENT_CODE, r.COMMENT, r. STATUS, " +
                "rsh.COMMENT COMMENT_STATUS, rsh.EVENT_DATETIME, rsh.USER_ACCOUNT_ID " +
                "FROM REQUESTS r " +
                "LEFT JOIN REQUEST_STATUS_HISTORY rsh ON r.ID = rsh.REQUEST_ID AND rsh.IS_LAST_STATUS = 1 " +
                "WHERE 1=1 " +
                (fromCreateDate != null ? " AND r.CREATE_DATE >= " + "'" + fromCreateDate + "'" : "") +
                (toCreateDate != null ? " AND r.CREATE_DATE <= " + "'" + toCreateDate + "'" : "") +
                (clientCode != null ? " AND r.CLIENT_CODE = " + "'" + clientCode + "'" : "") +
                (requestStatus != null ? " AND r.STATUS = " + "'" + requestStatus.name() + "'" : "");

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            Request request = new Request();
            request.setId(rs.getInt("id"));
            request.setRequestUUID(rs.getNString("request_uuid"));
            request.setCreateDate(new java.util.Date(rs.getDate("create_date").getTime()));
            request.setCreateDateTime(Timestamp.valueOf(rs.getNString("create_datetime")));
            request.setClientCode(rs.getNString("client_code"));
            request.setComment(rs.getNString("comment"));
            request.setRequestStatus(RequestStatusType.valueOf(rs.getNString("status")));
            request.setCommentRequestStatus(rs.getNString("COMMENT_STATUS"));
            request.setLastDateTimeChangeRequestStatus(Timestamp.valueOf(rs.getNString("EVENT_DATETIME")));
            request.setLastUserAccountIdChangeRequestStatus(rs.getInt("USER_ACCOUNT_ID"));
            requests.add(request);
        }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return requests;
    }

    public ArrayList<Audit> getAudits(int requestId) throws SQLException {
        logger.info("start");

//        try {
        String sql = "INNER JOIN REQUEST_XREF_AUDIT rxa ON a.ID = rxa.AUDIT_ID " +
                "WHERE rxa.REQUEST_ID = " + requestId;

        /*PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, requestId);*/

//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return new AuditService().getAudits(sql);
    }
}
