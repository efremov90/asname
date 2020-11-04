package org.asname.integration.requests.mq;

import java.io.IOException;

public interface RequestService {

    public void createRequest(CreateRequestRqType createRequestRq) throws IOException;

    public void cancelRequest(CancelRequestRqType cancelRequestRq) throws IOException;
}