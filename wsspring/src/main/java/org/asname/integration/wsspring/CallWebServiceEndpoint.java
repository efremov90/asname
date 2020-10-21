package org.asname.integration.wsspring;

import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.bind.annotation.XmlRootElement;

@Endpoint
public class CallWebServiceEndpoint {

    private static final String NAMESPACE_URI = "http://anil.hcl.com/calWebService/schemas";

    private CallWebService callWebService;

    @Autowired
    public CallWebServiceEndpoint(CallWebService callWebService) throws JDOMException {
        this.callWebService = callWebService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddRequest")
    public @ResponsePayload AddRsType
    add(@RequestPayload AddRqType addRequest) throws Exception {
        return callWebService.add(addRequest);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DiffRequest")
    public @ResponsePayload ResultType
    diff(@RequestPayload DiffRqType diffRequest) throws Exception {
        return callWebService.diff(diffRequest);
    }

}