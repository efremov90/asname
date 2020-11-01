package org.asname.integration.mq.mqone;

import org.asname.integration.requests.CancelRequestRqType;
import org.asname.integration.requests.CancelRequestRsType;
import org.asname.integration.requests.CreateRequestRqType;
import org.asname.integration.requests.CreateRequestRsType;

import java.io.IOException;

public interface RequestService {

    public CreateRequestRsType createRequest(CreateRequestRqType createRequestRq) throws IOException;

    public CancelRequestRsType cancelRequest(CancelRequestRqType cancelRequestRq) throws IOException;
}