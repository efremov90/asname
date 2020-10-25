package org.asname.integration.wsspring.wstwo;

import org.springframework.stereotype.Service;

@Service
public class CallWebServiceImpl implements CallWebService {
    @Override
    public AddRsType add(AddRqType addRq) {
        AddRsType resultType = new AddRsType();
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