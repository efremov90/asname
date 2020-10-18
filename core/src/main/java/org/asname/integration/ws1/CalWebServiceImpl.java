package org.asname.integration.ws1;

import javax.jws.WebService;

@WebService(endpointInterface = "org.asname.integration.ws1.CalWebService",
        portName = "calWebServicePort",
        serviceName = "calWebService")
public class CalWebServiceImpl implements CalWebService {
    @Override
    public ResultType add(AddRqType addRq) {
        ResultType resultType = new ResultType();
        resultType.setResult(addRq.getParam1()+addRq.getParam2());
        return resultType;
    }

    @Override
    public ResultType diff(DiffRqType diffRq) {
        ResultType resultType = new ResultType();
        resultType.setResult(diffRq.getParam1()-diffRq.getParam2());
        return resultType;
    }
}
