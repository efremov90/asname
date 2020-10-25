package org.asname.integration.wsspring.wsthree;

import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl implements RequestService {

    @Override
    public CreateRequestRsType createRequest(CreateRequestRqType createRequestRq) {
        CreateRequestRsType resultType = new CreateRequestRsType();
        resultType.setCode(0);
        return resultType;
    }

    @Override
    public CancelRequestRsType cancelRequest(CancelRequestRqType cancelRequestRq) {
        CancelRequestRsType resultType = new CancelRequestRsType();
        resultType.setCode(0);
        return resultType;
    }
}