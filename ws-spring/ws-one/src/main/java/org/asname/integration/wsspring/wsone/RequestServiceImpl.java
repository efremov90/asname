package org.asname.integration.wsspring.wsone;

import org.asname.integration.requests.ws.CancelRequestRqType;
import org.asname.integration.requests.ws.CancelRequestRsType;
import org.asname.integration.requests.ws.CreateRequestRqType;
import org.asname.integration.requests.ws.CreateRequestRsType;
import org.asname.model.requests.Request;
import org.asname.service.integration.IntegrationService;
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
            request.setRequestUUID(req.getRequestUUID());
            request.setCreateDate(Date.valueOf(req.getCreateDate().toString()));
            request.setCreateDateTime(new java.util.Date());
            request.setClientCode(req.getClientCode());
            request.setComment(req.getComment());
            request.setLastUserAccountIdChangeRequestStatus(-2);
            new org.asname.service.requests.RequestService().create(request,
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
            new org.asname.service.requests.RequestService().cancel(req.getRequestUUID(),
                    -2,
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