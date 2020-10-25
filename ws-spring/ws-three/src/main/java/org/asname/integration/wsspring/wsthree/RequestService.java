package org.asname.integration.wsspring.wsthree;

public interface RequestService {

    public CreateRequestRsType createRequest(CreateRequestRqType createRequestRq);

    public CancelRequestRsType cancelRequest(CancelRequestRqType cancelRequestRq);
}