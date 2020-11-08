package org.asname.integration.mq.receive.mqone;

import org.asname.integration.contract.requests.mq.*;

import java.io.IOException;

public interface RequestService {

    public void createRequestRq(CreateRequestRqType req) throws IOException;

    public void cancelRequestRq(CancelRequestRqType req) throws IOException;

    public void notifyRequestStatusRs(NotifyRequestStatusRsType res);
}