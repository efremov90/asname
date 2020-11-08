package org.asname.integration.mq.send.mqone;

import org.asname.integration.contract.requests.mq.*;

import java.io.IOException;

public interface RequestService {

    public void createRequestRs(CreateRequestRsType res);
    
    public void cancelRequestRs(CancelRequestRsType res);

    public void notifyRequestStatusRq(NotifyRequestStatusRqType req, int requestId) throws IOException;
}