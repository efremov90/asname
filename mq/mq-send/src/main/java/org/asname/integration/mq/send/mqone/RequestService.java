package org.asname.integration.mq.send.mqone;

import org.asname.integration.contract.requests.mq.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.MalformedURLException;

public interface RequestService {

    public void createRequestRs(CreateRequestRsType res, Exception exception) throws Exception;
    
    public void cancelRequestRs(CancelRequestRsType res, Exception exception) throws Exception;

    public void notifyRequestStatusRq(NotifyRequestStatusRqType req, int requestId, Exception exception) throws IOException;
}