package org.asname.integration.wsspring.wsone;

import org.asname.integration.requests.CancelRequestRqType;
import org.asname.integration.requests.CancelRequestRsType;
import org.asname.integration.requests.CreateRequestRqType;
import org.asname.integration.requests.CreateRequestRsType;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class RequestServiceEndpoint {

    private static final String NAMESPACE_URI = "http://org.asname.requests/schemas";

    private RequestService requestService;

    @Autowired
    public RequestServiceEndpoint(RequestService requestService) throws JDOMException {
        this.requestService = requestService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateRequestRq")
    public @ResponsePayload
    CreateRequestRsType
    createRequest(@RequestPayload CreateRequestRqType req) throws Exception {
        return requestService.createRequest(req);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CancelRequestRq")
    public @ResponsePayload
    CancelRequestRsType
    cancelRequest(@RequestPayload CancelRequestRqType cancelRequestRq) throws Exception {
        return requestService.cancelRequest(cancelRequestRq);
    }

}