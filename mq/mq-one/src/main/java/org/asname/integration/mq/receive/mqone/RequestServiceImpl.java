package org.asname.integration.mq.receive.mqone;

import org.asname.audit.dao.RequestAuditsDAO;
import org.asname.integration.contract.requests.mq.*;
import org.asname.integration.mq.log.service.MQLogService;
import org.asname.audit.model.AuditOperType;
import org.asname.model.requests.Request;
import org.asname.model.requests.RequestStatusType;
import org.asname.audit.service.AuditService;
import org.asname.integration.utils.service.IntegrationService;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;
import java.sql.Date;
import java.util.UUID;
import java.util.logging.Logger;

import static org.asname.audit.model.SystemType.ASNAME2;

@Service
public class RequestServiceImpl implements RequestService {

    private Logger logger = Logger.getLogger(RequestServiceImpl.class.getName());

    //C:\Users\NEO\.IntelliJIdea2019.3\system\tomcat
    protected final static String pathSchema = "/Requests.xsd";
    @Override
    public void createRequestRq(CreateRequestRqType req) throws IOException {
        Integer auditId = null;
        Integer requestId = null;
        try {
            try {

                auditId = new AuditService().create(
                        AuditOperType.MQ_IN_MESSAGE,
                        ASNAME2.getId(),
                        new java.util.Date(),
                        String.format("Создание заявки\n" +
                                        "UID сообщения: %s \n" +
                                        "UID заявки: %s \n",
                                req.getRqUID(),
                                req.getCreateRequest().getRequestUUID()),
                        req.getRqUID()
                );

                CreateRequestRsType res = new CreateRequestRsType();
                res.setRqUID(UUID.randomUUID().toString());
                res.setCorrelationUID(req.getRqUID());
                res.setRqTm(new IntegrationService().getXMLGregorianCalendar(new java.util.Date()));
                StatusType statusType = new StatusType();
                statusType.setCode(0);
                res.setStatus(statusType);
                new org.asname.integration.mq.send.mqone.RequestServiceImpl().createRequestRs(res,null);

            } catch (Exception e) {

                CreateRequestRsType res = new CreateRequestRsType();
                res.setRqUID(UUID.randomUUID().toString());
                res.setCorrelationUID(req.getRqUID());
                res.setRqTm(new IntegrationService().getXMLGregorianCalendar(new java.util.Date()));
                StatusType statusType = new StatusType();
                statusType.setCode(-1);
                statusType.setDescription(new IntegrationService().getExceptionString(e));
                res.setStatus(statusType);
                new org.asname.integration.mq.send.mqone.RequestServiceImpl().createRequestRs(res,e);

                throw new Exception(e);
            }

            Request request = new Request();
            request.setRequestUUID(req.getCreateRequest().getRequestUUID());
            request.setCreateDate(Date.valueOf(req.getCreateRequest().getCreateDate().toString()));
            request.setCreateDateTime(new java.util.Date());
            request.setClientCode(req.getCreateRequest().getClientCode());
            request.setComment(req.getCreateRequest().getComment());
            request.setLastUserAccountIdChangeRequestStatus(ASNAME2.getId());
            request.setCreateSystemId(ASNAME2.getId());
            requestId = new org.asname.service.requests.RequestService().create(request,
                    ASNAME2.getId());

            new RequestAuditsDAO().create(requestId, auditId);

            NotifyRequestStatusRqType notify = new NotifyRequestStatusRqType();
            notify.setRqUID(UUID.randomUUID().toString());
            notify.setCorrelationUID(req.getRqUID());
            notify.setRqTm(new IntegrationService().getXMLGregorianCalendar(new java.util.Date()));
            NotifyRequestStatusRequestType notifyRequest = new NotifyRequestStatusRequestType();
            notifyRequest.setRequestUUID(req.getCreateRequest().getRequestUUID());
            notifyRequest.setStatus(RequestStatusType.CREATED.name());
            notify.setNotifyRequestStatusRequest(notifyRequest);
            new org.asname.integration.mq.send.mqone.RequestServiceImpl().notifyRequestStatusRq(notify,requestId,null);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                NotifyRequestStatusRqType notify = new NotifyRequestStatusRqType();
                notify.setRqUID(UUID.randomUUID().toString());
                notify.setCorrelationUID(req.getRqUID());
                notify.setRqTm(new IntegrationService().getXMLGregorianCalendar(new java.util.Date()));
                NotifyRequestStatusRequestType notifyRequest = new NotifyRequestStatusRequestType();
                notifyRequest.setRequestUUID(req.getCreateRequest().getRequestUUID());
                notifyRequest.setStatus(RequestStatusType.ERROR.name());
                notifyRequest.setComment(e.getMessage());
                notify.setNotifyRequestStatusRequest(notifyRequest);
                new org.asname.integration.mq.send.mqone.RequestServiceImpl().notifyRequestStatusRq(notify,-1,e);
            } catch (DatatypeConfigurationException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void cancelRequestRq(CancelRequestRqType req) throws IOException {
        Integer auditId = null;
        Integer requestId = null;
        try {
            try {

                auditId = new AuditService().create(
                        AuditOperType.MQ_IN_MESSAGE,
                        ASNAME2.getId(),
                        new java.util.Date(),
                        String.format("Отмена заявки\n" +
                                        "UID сообщения: %s \n" +
                                        "UID заявки: %s \n",
                                req.getRqUID(),
                                req.getCancelRequest().getRequestUUID()),
                        req.getRqUID()
                );

                CancelRequestRsType res = new CancelRequestRsType();
                res.setRqUID(UUID.randomUUID().toString());
                res.setCorrelationUID(req.getRqUID());
                res.setRqTm(new IntegrationService().getXMLGregorianCalendar(new java.util.Date()));
                StatusType statusType = new StatusType();
                statusType.setCode(0);
                res.setStatus(statusType);
                new org.asname.integration.mq.send.mqone.RequestServiceImpl().cancelRequestRs(res,null);

            } catch (Exception e) {

                CancelRequestRsType res = new CancelRequestRsType();
                res.setRqUID(UUID.randomUUID().toString());
                res.setCorrelationUID(req.getRqUID());
                res.setRqTm(new IntegrationService().getXMLGregorianCalendar(new java.util.Date()));
                StatusType statusType = new StatusType();
                statusType.setCode(-1);
                statusType.setDescription(new IntegrationService().getExceptionString(e));
                res.setStatus(statusType);
                new org.asname.integration.mq.send.mqone.RequestServiceImpl().cancelRequestRs(res,e);

                throw new Exception(e);
            }

            requestId =
                    new org.asname.service.requests.RequestService().getRequestIdByUUID(req.getCancelRequest().getRequestUUID());
            if (requestId != null) new RequestAuditsDAO().create(requestId, auditId);

            new org.asname.service.requests.RequestService().cancel(req.getCancelRequest().getRequestUUID(),
                    ASNAME2.getId(),
                    req.getCancelRequest().getComment());

            NotifyRequestStatusRqType notify = new NotifyRequestStatusRqType();
            notify.setCorrelationUID(req.getRqUID());
            NotifyRequestStatusRequestType notifyRequest = new NotifyRequestStatusRequestType();
            notifyRequest.setRequestUUID(req.getCancelRequest().getRequestUUID());
            notifyRequest.setStatus(RequestStatusType.CANCELED.name());
            notifyRequest.setComment(new org.asname.service.requests.RequestService().getRequestByUUID(req.getCancelRequest().getRequestUUID()).getCommentRequestStatus());
            notify.setNotifyRequestStatusRequest(notifyRequest);
            new org.asname.integration.mq.send.mqone.RequestServiceImpl().notifyRequestStatusRq(notify,requestId,null);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                NotifyRequestStatusRqType notify = new NotifyRequestStatusRqType();
                notify.setRqUID(UUID.randomUUID().toString());
                notify.setCorrelationUID(req.getRqUID());
                notify.setRqTm(new IntegrationService().getXMLGregorianCalendar(new java.util.Date()));
                NotifyRequestStatusRequestType notifyRequest = new NotifyRequestStatusRequestType();
                notifyRequest.setRequestUUID(req.getCancelRequest().getRequestUUID());
                notifyRequest.setStatus(RequestStatusType.ERROR.name());
                notifyRequest.setComment(e.getMessage());
                notify.setNotifyRequestStatusRequest(notifyRequest);
                new org.asname.integration.mq.send.mqone.RequestServiceImpl().notifyRequestStatusRq(notify,-1,e);
            } catch (DatatypeConfigurationException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void notifyRequestStatusRs(NotifyRequestStatusRsType res) {
    }
}