package org.asname.integration.wsspring.wsone;

import org.asname.model.Request;
import org.asname.service.WSService;
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
            request.setLastUserAccountIdChangeRequestStatus(-2);
            new org.asname.service.RequestService().create(request,
                    -2);
            resp.setCode(0);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.setCode(-1);
            resp.setDescription(new WSService().getExceptionString(e));
            return resp;
        }
    }

    @Override
    public CancelRequestRsType cancelRequest(CancelRequestRqType req) throws IOException {
        CancelRequestRsType resp = new CancelRequestRsType();
        try {
            new org.asname.service.RequestService().cancel(req.requestUUID,
                    -2,
                    req.getComment());
            resp.setCode(0);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.setCode(-1);
            resp.setDescription(new WSService().getExceptionString(e));
            return resp;
        }
    }
}