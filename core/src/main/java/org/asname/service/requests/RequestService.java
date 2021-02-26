package org.asname.service.requests;

import org.asname.audit.dao.RequestAuditsDAO;
import org.asname.dao.requests.RequestDAO;
import org.asname.dao.requests.RequestStatusHistoryDAO;
import org.asname.dao.requests.RequestTasksDAO;
import org.asname.dao.users.UserAccountDAO;
import org.asname.db.connection.MySQLConnection;
import org.asname.audit.model.Audit;
import org.asname.audit.model.AuditOperType;
import org.asname.integration.contract.requests.mq.NotifyRequestStatusRequestType;
import org.asname.integration.contract.requests.mq.NotifyRequestStatusRqType;
import org.asname.integration.mq.send.mqone.RequestServiceImpl;
import org.asname.model.requests.Request;
import org.asname.model.requests.RequestStatusHistory;
import org.asname.model.requests.RequestStatusType;
import org.asname.model.tasks.Task;
import org.asname.model.tasks.TaskStatusType;
import org.asname.model.users.UserAccount;
import org.asname.audit.service.AuditService;
import org.asname.service.security.PermissionService;
import org.asname.service.TaskService;
import org.asname.service.clients.ClientService;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.asname.audit.model.SystemType.ASNAME2;
import static org.asname.model.configure.Configures.CANCEL_REQUEST_INTERVAL;
import static org.asname.model.security.Permissions.*;
import static org.asname.model.requests.RequestStatusType.*;
import static org.asname.model.tasks.TaskType.CANCEL_REQUEST;

public class RequestService {

    private Connection conn;
    private Logger logger = Logger.getLogger(RequestService.class.getName());

    public RequestService() {
        logger.info("start");

        conn = MySQLConnection.getConnection();
    }

    public void validateExistsRequest(String requestUUID) throws Exception {
        logger.info("start");

        if (getRequestIdByUUID(requestUUID) != null)
            throw new Exception(String.format("Уже есть заявка с UUID %s.",
                    requestUUID));

    }

    public Integer getRequestIdByUUID(String requestUUID) throws SQLException {
        logger.info("start");

        Integer id = null;

        String sql = "SELECT ID  " +
                "FROM REQUESTS r " +
                "WHERE REQUEST_UUID = ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, requestUUID);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            id = rs.getInt("id");
        }

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

        String sql = "SELECT " +
                "r.ID, r.REQUEST_UUID, r.CREATE_DATE, r.CREATE_DATETIME, r.CLIENT_CODE, r.COMMENT, r.STATUS, " +
                "rsh.COMMENT STATUS_COMMENT, r.CREATE_SYSTEM_ID, rsh.EVENT_DATETIME, rsh.USER_ACCOUNT_ID " +
                "FROM REQUESTS r " +
                "LEFT JOIN REQUEST_STATUS_HISTORY rsh ON r.ID = rsh.REQUEST_ID AND rsh.IS_LAST_STATUS = 1 " +
                "WHERE 1=1 " +
                (requestId != -1 ? " AND r.ID = " + requestId : "") +
                (requestUUID != null ? " AND r.REQUEST_UUID = " + "'" + requestUUID.toLowerCase() + "'" : "");

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
            request.setCommentRequestStatus(rs.getNString("STATUS_COMMENT"));
            request.setCreateSystemId(rs.getInt("create_system_id"));
            request.setLastDateTimeChangeRequestStatus(Timestamp.valueOf(rs.getNString("event_datetime")));
            request.setLastUserAccountIdChangeRequestStatus(rs.getInt("user_account_id"));
        }

        return request;
    }

    public Integer create(Request request, int userAccountId) throws Exception {
        logger.info("start");

        Integer result = null;
        boolean dontExecute = false;
        Request existingRequest = null;

        UserAccount userAccount = new UserAccountDAO().getUserAccountById(userAccountId);
        if (!new PermissionService().isPermission(userAccountId, REQUESTS_CREATE))
            throw new Exception(String.format("У пользователя %s отсутствует разрешение %s.",
                    userAccount.getAccount(),
                    REQUESTS_CREATE.name()));
        if (getRequestByUUID(request.getRequestUUID()) != null) {
            existingRequest = getRequestByUUID(request.getRequestUUID());
            result=existingRequest.getId();
            if (request.getCreateSystemId()!=existingRequest.getCreateSystemId())
                throw new Exception(String.format("Уже есть заявка с UUID %s.",
                        request.getRequestUUID()));
            //заявка повторно создается системой-создателем
            else dontExecute=true;
        }
        if (request.getClientCode() == null || request.getClientCode() == "")
            throw new Exception(String.format("Не задан код клиента."));
        if (new ClientService().getClient(request.getClientCode()) == null)
            throw new Exception(String.format("Не найден клиент с кодом %s.",
                    request.getClientCode()));

        //dontExecute - не выполнять, потому что см. выше
        if (!dontExecute) {

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
        }

        //Отправка нотификации при создании через MQ системой-создателем
        if ((request.getCreateSystemId() == ASNAME2.getId()) && (userAccountId == ASNAME2.getId())) {
            NotifyRequestStatusRqType notify = new NotifyRequestStatusRqType();
            NotifyRequestStatusRequestType notifyRequest = new NotifyRequestStatusRequestType();
            notifyRequest.setRequestUUID(request.getRequestUUID());
            notifyRequest.setStatus((existingRequest!=null) ? existingRequest.getRequestStatus().name() : CREATED.toString());
            notifyRequest.setComment((existingRequest!=null) ? existingRequest.getCommentRequestStatus() : null);
            notify.setNotifyRequestStatusRequest(notifyRequest);
            new RequestServiceImpl().notifyRequestStatusRq(notify, (existingRequest!=null) ? existingRequest.getId() : result,null);
        }

        return result;
    }

    public boolean cancel(String requestUUID, int userAccountId, String comment) throws Exception {
        logger.info("start");

        boolean result = false;
        boolean dontExecute = false;

        UserAccount userAccount = new UserAccountDAO().getUserAccountById(userAccountId);
        if (!new PermissionService().isPermission(userAccountId, REQUESTS_CANCEL))
            throw new Exception(String.format("У пользователя %s отсутствует разрешение %s.",
                    userAccount.getAccount(),
                    REQUESTS_CANCEL.name()));
        if (getRequestIdByUUID(requestUUID) == null)
            throw new Exception(String.format("Заявка с UUID %s отсутствует.",
                    requestUUID));

        Request request = new RequestService().getRequestByUUID(requestUUID);

        if (getRequestByUUID(request.getRequestUUID()) != null) {
            Request existingRequest = getRequestByUUID(request.getRequestUUID());
            if (request.getCreateSystemId()!=existingRequest.getCreateSystemId())
                throw new Exception(String.format("Система-создатель id %s не соответствует id %s.",
                        request.getCreateSystemId(),
                        existingRequest.getCreateSystemId()));
        }

        if (getRequestByUUID(requestUUID).getRequestStatus() != CREATED)
            if (request.getCreateSystemId() != ASNAME2.getId())
                throw new Exception(String.format("Заявка в статусе %s.",
                        request.getRequestStatus().getDescription()));
            //заявка отменяется системой-создателем и она в не доступном статусе
            else dontExecute = true;

        //dontExecute - не выполнять, потому что см. выше
        if (!dontExecute) {

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
        }

        Request existingRequest = getRequestByUUID(request.getRequestUUID());

        //Отправка нотификации при отмене не системой-создателем
        if (request.getCreateSystemId() == ASNAME2.getId()) {
            NotifyRequestStatusRqType notify = new NotifyRequestStatusRqType();
            NotifyRequestStatusRequestType notifyRequest = new NotifyRequestStatusRequestType();
            notifyRequest.setRequestUUID(requestUUID);
            notifyRequest.setStatus((existingRequest!=null) ? existingRequest.getRequestStatus().name() : CREATED.toString());
            notifyRequest.setComment((existingRequest!=null) ? existingRequest.getCommentRequestStatus() : null);
            notify.setNotifyRequestStatusRequest(notifyRequest);
            new RequestServiceImpl().notifyRequestStatusRq(notify, request.getId(),null);
        }

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

        String sql = "SELECT " +
                "r.ID, r.REQUEST_UUID, r.CREATE_DATE, r.CREATE_DATETIME, r.CLIENT_CODE, r.COMMENT, r. STATUS, " +
                "r.CREATE_SYSTEM_ID, rsh.COMMENT COMMENT_STATUS, rsh.EVENT_DATETIME, rsh.USER_ACCOUNT_ID " +
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
            request.setCreateSystemId(rs.getInt("create_system_id"));
            request.setCommentRequestStatus(rs.getNString("COMMENT_STATUS"));
            request.setLastDateTimeChangeRequestStatus(Timestamp.valueOf(rs.getNString("EVENT_DATETIME")));
            request.setLastUserAccountIdChangeRequestStatus(rs.getInt("USER_ACCOUNT_ID"));
            requests.add(request);
        }

        return requests;
    }

    public ArrayList<Audit> getAudits(int requestId) throws SQLException {
        logger.info("start");

        String sql = "INNER JOIN REQUEST_XREF_AUDIT rxa ON a.ID = rxa.AUDIT_ID " +
                "WHERE rxa.REQUEST_ID = " + requestId;

        return new AuditService().getAudits(sql);
    }
}
