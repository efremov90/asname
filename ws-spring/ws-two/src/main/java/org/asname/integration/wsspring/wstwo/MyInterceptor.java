package org.asname.integration.wsspring.wstwo;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MyInterceptor implements EndpointInterceptor {
    @Override
    public boolean handleRequest(MessageContext messageContext, Object o) throws Exception {
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object o) throws Exception {
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object o) throws Exception {
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object o, Exception e) throws Exception {
        System.out.println("messageContext:"+messageContext.toString());
        System.out.println("getRequest:"+messageContext.getRequest().toString());
        System.out.println("getPayloadSource:"+messageContext.getRequest().getPayloadSource().toString());
        System.out.println("getSystemId:"+messageContext.getRequest().getPayloadSource().getSystemId());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        messageContext.getRequest().writeTo(baos);
        System.out.println("getRequest.writeTo:"+baos.toString());
        messageContext.getResponse().writeTo(baos);
        System.out.println("getResponse.writeTo:"+baos.toString());
        

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringBuilder xmlStringBuilder = new StringBuilder();
        messageContext.getRequest().writeTo(baos);
        xmlStringBuilder.append(baos.toString());
        ByteArrayInputStream input = new ByteArrayInputStream(
                xmlStringBuilder.toString().getBytes("UTF-8"));
        Document doc = builder.parse(input);
        System.out.println("body:"+doc.getElementsByTagName("body").toString());
    }
}
