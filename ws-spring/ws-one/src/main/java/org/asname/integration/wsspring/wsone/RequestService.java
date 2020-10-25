package org.asname.integration.wsspring.wsone;

import java.io.IOException;

public interface RequestService {

    public CreateRequestRsType createRequest(CreateRequestRqType createRequestRq) throws IOException;

    public CancelRequestRsType cancelRequest(CancelRequestRqType cancelRequestRq) throws IOException;
}