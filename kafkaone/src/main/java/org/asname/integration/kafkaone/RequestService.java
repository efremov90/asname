package org.asname.integration.kafkaone;

import org.asname.integration.contract.requests.kafkaone.*;

import java.io.IOException;

public interface RequestService {

    public void createRequestRq(CreateRequestRq req) throws IOException;

    public void cancelRequestRq(CancelRequestRq req) throws IOException;

    public void createRequestRs(CreateRequestRs res, Exception exception) throws Exception;

    public void cancelRequestRs(CancelRequestRs res, Exception exception) throws Exception;

    public void notifyRequestStatusRq(NotifyRequestStatusRq req, int requestId, Exception exception) throws IOException;
}