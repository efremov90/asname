package org.asname.integration.ws;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(endpointInterface = "org.asname.integration.ws.CalWebService",
        portName = "calWebServicePort",
        serviceName = "calWebService")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
        ObjectFactory.class
})
public class CalWebServiceImpl implements CalWebService {
    @Override
    public ResultType add(AddRqType addRq) {
        ResultType resultType = new ResultType();
        resultType.result = addRq.param1+addRq.param2;
        return resultType;
    }

    @Override
    public ResultType diff(DiffRqType diffRq) {
        ResultType resultType = new ResultType();
        resultType.result = diffRq.param1-diffRq.param2;
        return resultType;
    }
}
