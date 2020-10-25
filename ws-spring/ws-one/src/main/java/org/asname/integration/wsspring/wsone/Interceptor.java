package org.asname.integration.wsspring.wsone;

import org.asname.model.WSLog;
import org.asname.model.WSLogDirectionType;
import org.asname.model.WSLogStatusType;
import org.asname.service.WSLogService;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class Interceptor implements EndpointInterceptor {

    private Date StartDatetime = null;
    private Date EndDatetime = null;
    private WSLogStatusType wsLogStatusType;

    @Override
    public boolean handleRequest(MessageContext messageContext, Object o) throws Exception {
        StartDatetime = new Date();
        wsLogStatusType = WSLogStatusType.RECEIVED;
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object o) throws Exception {
        EndDatetime = new Date();
        wsLogStatusType = WSLogStatusType.OK;
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
        wsLog.setDirection(WSLogDirectionType.IN);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        messageContext.getRequest().writeTo(baos);
        wsLog.setRequest(new WSLogService().transformXML(baos.toString()));
        baos.reset();
        messageContext.getResponse().writeTo(baos);
        wsLog.setResponse(new WSLogService().transformXML(baos.toString()));
        wsLog.setMethod(new WSLogService().getMethod(messageContext.getRequest().toString()).name());
        wsLog.setStatus(wsLogStatusType);
        wsLog.setStartDatetime(StartDatetime);
        wsLog.setEndDatetime(EndDatetime);
        new WSLogService().create(wsLog);
    }
}
