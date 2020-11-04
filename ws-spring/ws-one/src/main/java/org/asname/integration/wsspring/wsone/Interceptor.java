package org.asname.integration.wsspring.wsone;

import org.asname.model.integration.WSLog;
import org.asname.model.integration.DirectionType;
import org.asname.model.integration.StatusType;
import org.asname.service.integration.IntegrationService;
import org.asname.service.integration.WSLogService;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class Interceptor implements EndpointInterceptor {

    private Date StartDatetime = null;
    private Date EndDatetime = null;
    private StatusType status;

    @Override
    public boolean handleRequest(MessageContext messageContext, Object o) throws Exception {
        StartDatetime = new Date();
        status = StatusType.RECEIVED;
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object o) throws Exception {
        EndDatetime = new Date();
        status = StatusType.OK;
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object o) throws Exception {
        System.out.println("handleFault");
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object o, Exception e) throws Exception {
        WSLog wsLog = new WSLog();
        wsLog.setDirection(DirectionType.IN);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        messageContext.getRequest().writeTo(baos);
        wsLog.setRequest(new IntegrationService().transformXML(baos.toString()));
        baos.reset();
        messageContext.getResponse().writeTo(baos);
        wsLog.setResponse(new IntegrationService().transformXML(baos.toString()));
        wsLog.setMethod(new IntegrationService().getMethod(messageContext.getRequest().toString()).name());
        wsLog.setStatus(status);
        wsLog.setStartDatetime(StartDatetime);
        wsLog.setEndDatetime(EndDatetime);
        new WSLogService().create(wsLog);
    }
}
