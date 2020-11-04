package org.asname.integration.wsspring.wsone;

import org.asname.integration.requests.ws.CancelRequestRqType;
import org.asname.integration.requests.ws.CancelRequestRsType;
import org.asname.integration.requests.ws.CreateRequestRqType;
import org.asname.integration.requests.ws.CreateRequestRsType;

import java.io.IOException;

public interface RequestService {

    public CreateRequestRsType createRequest(CreateRequestRqType createRequestRq) throws IOException;

    public CancelRequestRsType cancelRequest(CancelRequestRqType cancelRequestRq) throws IOException;
}