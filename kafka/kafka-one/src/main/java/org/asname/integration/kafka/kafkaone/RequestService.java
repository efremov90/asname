package org.asname.integration.kafka.kafkaone;

import org.asname.integration.contract.requests.kafka.*;

import java.io.IOException;

public interface RequestService {

    public void createRequestRq(CreateRequestRq req) throws IOException;

    public void cancelRequestRq(CancelRequestRq req) throws IOException;
}