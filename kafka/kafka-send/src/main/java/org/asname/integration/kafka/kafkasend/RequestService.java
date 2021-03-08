package org.asname.integration.kafka.kafkasend;

import org.asname.integration.contract.requests.kafka.*;

import java.io.IOException;

public interface RequestService {

    public void createRequestRs(CreateRequestRs res, Exception exception) throws Exception;

    public void cancelRequestRs(CancelRequestRs res, Exception exception) throws Exception;

    public void notifyRequestStatusRq(NotifyRequestStatusRq req, int requestId, Exception exception) throws IOException;
}