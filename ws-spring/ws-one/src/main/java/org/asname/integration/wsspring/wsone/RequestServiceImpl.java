package org.asname.integration.wsspring.wsone;

import org.asname.integration.contract.requests.ws.CancelRequestRqType;
import org.asname.integration.contract.requests.ws.CancelRequestRsType;
import org.asname.integration.contract.requests.ws.CreateRequestRqType;
import org.asname.integration.contract.requests.ws.CreateRequestRsType;
import org.asname.model.requests.Request;
import org.asname.integration.utils.service.IntegrationService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;

import static org.asname.audit.model.SystemType.ASNAME1;

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
            request.setLastUserAccountIdChangeRequestStatus(ASNAME1.getId());
            request.setCreateSystemId(ASNAME1.getId());
            new org.asname.service.requests.RequestService().create(request,
                    ASNAME1.getId());
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
                    ASNAME1.getId(),
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