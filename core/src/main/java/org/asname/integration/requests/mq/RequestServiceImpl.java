package org.asname.integration.requests.mq;

import org.asname.dao.requests.RequestAuditsDAO;
import org.asname.model.audit.AuditOperType;
import org.asname.model.requests.Request;
import org.asname.service.audit.AuditService;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class RequestServiceImpl implements RequestService {

    @Override
    public void createRequest(CreateRequestRqType req) {
        try {

            Integer auditId = new AuditService().create(
                    AuditOperType.MQ_IN_MESSAGE,
                    -3,
                    new java.util.Date(),
                    String.format("UID сообщения: %s \n" +
                                    "UID заявки: %s \n",
                            req.rqUID,
                            req.createRequest.requestUUID),
                    req.rqUID
            );

            Request request = new Request();
            request.setRequestUUID(req.getCreateRequest().getRequestUUID());
            request.setCreateDate(Date.valueOf(req.getCreateRequest().getCreateDate().toString()));
            request.setCreateDateTime(new java.util.Date());
            request.setClientCode(req.getCreateRequest().getClientCode());
            request.setComment(req.getCreateRequest().getComment());
            request.setLastUserAccountIdChangeRequestStatus(-3);
            Integer requestId = new org.asname.service.requests.RequestService().create(request,
                    -3);

            new RequestAuditsDAO().create(requestId, auditId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelRequest(CancelRequestRqType req) {
        try {

            Integer auditId = new AuditService().create(
                    AuditOperType.MQ_IN_MESSAGE,
                    -3,
                    new java.util.Date(),
                    String.format("UID сообщения: %s \n" +
                                    "UID заявки: %s \n",
                            req.rqUID,
                            req.cancelRequest.requestUUID),
                    req.rqUID
            );

            Integer requestId =
                    new org.asname.service.requests.RequestService().getRequestIdByUUID(req.cancelRequest.requestUUID);
            if (requestId != null) new RequestAuditsDAO().create(requestId, auditId);

            new org.asname.service.requests.RequestService().cancel(req.getCancelRequest().getRequestUUID(),
                    -3,
                    req.getCancelRequest().getComment());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}