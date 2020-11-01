package org.asname.integration.mq.mqone;

import org.asname.integration.requests.CancelRequestRqType;
import org.asname.integration.requests.CancelRequestRsType;
import org.asname.integration.requests.CreateRequestRqType;
import org.asname.integration.requests.CreateRequestRsType;
import org.asname.model.Request;
import org.asname.service.IntegrationService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;

@Service
public class RequestServiceImpl implements RequestService {

    @Override
    public CreateRequestRsType createRequest(CreateRequestRqType req) throws IOException {
        CreateRequestRsType resp = new CreateRequestRsType();
        try {
            Request request = new Request();
            request.setId(0);
            request.setRequestUUID(req.getRequestUUID());
            request.setCreateDate(Date.valueOf(req.getCreateDate().toString()));
            request.setCreateDateTime(new java.util.Date());
            request.setClientCode(req.getClientCode());
            request.setComment(req.getComment());
            request.setLastUserAccountIdChangeRequestStatus(-3);
            new org.asname.service.RequestService().create(request,
                    -2);
            resp.setCode(0);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.setCode(-1);
            resp.setDescription(new IntegrationService().getExceptionString(e));
            return resp;
        }
    }

    @Override
    public CancelRequestRsType cancelRequest(CancelRequestRqType req) throws IOException {
        CancelRequestRsType resp = new CancelRequestRsType();
        try {
            new org.asname.service.RequestService().cancel(req.getRequestUUID(),
                    -3,
                    req.getComment());
            resp.setCode(0);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.setCode(-1);
            resp.setDescription(new IntegrationService().getExceptionString(e));
            return resp;
        }
    }
}