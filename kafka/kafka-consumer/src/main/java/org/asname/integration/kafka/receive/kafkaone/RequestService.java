package org.asname.integration.kafka.receive.kafkaone;

import org.asname.integration.contract.requests.mq.CancelRequestRqType;
import org.asname.integration.contract.requests.mq.CreateRequestRqType;
import org.asname.integration.contract.requests.mq.NotifyRequestStatusRsType;

import java.io.IOException;

public interface RequestService {

    public void createRequestRq(CreateRequestRqType req) throws IOException;

    public void cancelRequestRq(CancelRequestRqType req) throws IOException;

    public void notifyRequestStatusRs(NotifyRequestStatusRsType res);
}