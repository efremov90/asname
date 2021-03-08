package org.asname.integration.kafka.kafkaone;

import org.asname.audit.dao.RequestAuditsDAO;
import org.asname.audit.model.AuditOperType;
import org.asname.audit.model.SystemType;
import org.asname.audit.service.AuditService;
import org.asname.integration.contract.requests.kafka.*;
import org.asname.integration.utils.service.IntegrationService;
import org.asname.model.requests.Request;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class RequestServiceImpl implements RequestService {

    private Logger logger = Logger.getLogger(RequestServiceImpl.class.getName());

    //C:\Users\NEO\.IntelliJIdea2019.3\system\tomcat
    protected final static String pathSchema = "/Requests.json";
    @Override
    public void createRequestRq(CreateRequestRq req) throws IOException {
        Integer auditId = null;
        Integer requestId = null;
        try {
            try {

                auditId = new AuditService().create(
                        AuditOperType.MQ_IN_MESSAGE,
                        SystemType.ASNAME3.getId(),
                        new java.util.Date(),
                        String.format("Создание заявки\n" +
                                        "UID сообщения: %s \n" +
                                        "UID заявки: %s \n",
                                req.getHeader().getRqUID(),
                                req.getCreateRequest().getRequestUUID()),
                        req.getHeader().getRqUID()
                );

                CreateRequestRs res = new CreateRequestRs();
                Header header = new Header();
                header.setCorrelationUID(req.getHeader().getRqUID());
                Status statusType = new Status();
                statusType.setCode(0);
                res.setHeader(header);
                res.setStatus(statusType);
                new org.asname.integration.kafka.kafkasend.RequestServiceImpl().createRequestRs(res,null);

            } catch (Exception e) {

                CreateRequestRs res = new CreateRequestRs();
                Header header = new Header();
                header.setCorrelationUID(req.getHeader().getRqUID());
                Status statusType = new Status();
                statusType.setCode(-1);
                statusType.setDescription(new IntegrationService().getExceptionString(e));
                res.setHeader(header);
                res.setStatus(statusType);
                new org.asname.integration.kafka.kafkasend.RequestServiceImpl().createRequestRs(res,e);

                throw new Exception(e);
            }

            Request request = new Request();
            request.setRequestUUID(req.getCreateRequest().getRequestUUID());
            request.setCreateDate(Date.valueOf(req.getCreateRequest().getCreateDate().toString()));
            request.setCreateDateTime(new java.util.Date());
            request.setClientCode(req.getCreateRequest().getClientCode());
            request.setComment(req.getCreateRequest().getComment());
            request.setLastUserAccountIdChangeRequestStatus(SystemType.ASNAME3.getId());
            request.setCreateSystemId(SystemType.ASNAME3.getId());
            requestId = new org.asname.service.requests.RequestService().create(request,
                    SystemType.ASNAME3.getId());

            new RequestAuditsDAO().create(requestId, auditId);

        } catch (Exception e) {
            e.printStackTrace();
            NotifyRequestStatusRq notify = new NotifyRequestStatusRq();
            Header header = new Header();
            header.setCorrelationUID(req.getHeader().getRqUID());
            NotifyRequestStatusRequest notifyRequest = new NotifyRequestStatusRequest();
            notifyRequest.setRequestUUID(req.getCreateRequest().getRequestUUID());
            notifyRequest.setStatus(NotifyRequestStatusRequest.Status.ERROR);
            notifyRequest.setComment(e.getMessage());
            notify.setHeader(header);
            notify.setNotifyRequestStatusRequest(notifyRequest);
            new org.asname.integration.kafka.kafkasend.RequestServiceImpl().notifyRequestStatusRq(notify,-1,e);
        }
    }

    @Override
    public void cancelRequestRq(CancelRequestRq req) throws IOException {
        Integer auditId = null;
        Integer requestId = null;
        try {
            try {

                auditId = new AuditService().create(
                        AuditOperType.MQ_IN_MESSAGE,
                        SystemType.ASNAME3.getId(),
                        new java.util.Date(),
                        String.format("Отмена заявки\n" +
                                        "UID сообщения: %s \n" +
                                        "UID заявки: %s \n",
                                req.getHeader().getRqUID(),
                                req.getCancelRequest().getRequestUUID()),
                        req.getHeader().getRqUID()
                );

                CancelRequestRs res = new CancelRequestRs();
                Header header = new Header();
                header.setCorrelationUID(req.getHeader().getRqUID());
                Status statusType = new Status();
                statusType.setCode(0);
                res.setHeader(header);
                res.setStatus(statusType);
                new org.asname.integration.kafka.kafkasend.RequestServiceImpl().cancelRequestRs(res,null);

            } catch (Exception e) {

                CancelRequestRs res = new CancelRequestRs();
                Header header = new Header();
                header.setCorrelationUID(req.getHeader().getRqUID());
                Status statusType = new Status();
                statusType.setCode(-1);
                statusType.setDescription(new IntegrationService().getExceptionString(e));
                res.setHeader(header);
                res.setStatus(statusType);
                new org.asname.integration.kafka.kafkasend.RequestServiceImpl().cancelRequestRs(res,e);

                throw new Exception(e);
            }

            requestId =
                    new org.asname.service.requests.RequestService().getRequestIdByUUID(req.getCancelRequest().getRequestUUID());
            if (requestId != null) new RequestAuditsDAO().create(requestId, auditId);

            new org.asname.service.requests.RequestService().cancel(req.getCancelRequest().getRequestUUID(),
                    SystemType.ASNAME3.getId(),
                    req.getCancelRequest().getComment());

        } catch (Exception e) {
            e.printStackTrace();
            NotifyRequestStatusRq notify = new NotifyRequestStatusRq();
            Header header = new Header();
            header.setCorrelationUID(req.getHeader().getRqUID());
            NotifyRequestStatusRequest notifyRequest = new NotifyRequestStatusRequest();
            notifyRequest.setRequestUUID(req.getCancelRequest().getRequestUUID());
            notifyRequest.setStatus(NotifyRequestStatusRequest.Status.ERROR);
            notifyRequest.setComment(e.getMessage());
            notify.setHeader(header);
            notify.setNotifyRequestStatusRequest(notifyRequest);
            new org.asname.integration.kafka.kafkasend.RequestServiceImpl().notifyRequestStatusRq(notify,-1,e);
        }
    }
}