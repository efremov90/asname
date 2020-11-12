package org.asname.integration.mq.receive.mqone;

import org.asname.integration.contract.requests.mq.CreateRequestRqType;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DemoTest {
    public static void main(String[] args) throws Exception {

        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns2:NotifyRequestStatusRq xmlns:ns2=\"http://org.asname.requests/schemas\">\n" +
                "    <RqUID>a55d2e29-29e0-4ccc-b95a-25f2734ea674</RqUID>\n" +
                "    <RqTm>2020-11-12T00:17:24.832+03:00</RqTm>\n" +
                "    <CorrelationUID>106757a0-162c-4c30-a5c8-dfe8a1a1f415</CorrelationUID>\n" +
                "    <NotifyRequestStatusRequest>\n" +
                "        <RequestUUID>106757a0-162c-4c30-a5c8-dfe8a1a1f414</RequestUUID>\n" +
                "        <Status>ERROR</Status>\n" +
                "        <Comment>java.lang.Exception: РЈР¶Рµ РµСЃС‚СЊ Р·Р°СЏРІРєР° СЃ UUID 106757a0-162c-4c30-a5c8-dfe8a1a1f414.\n" +
                "org.asname.service.requests.RequestService.create(RequestService.java:131)\n" +
                "org.asname.integration.mq.receive.mqone.RequestServiceImpl.createRequestRq(RequestServiceImpl.java:79)\n" +
                "org.asname.integration.mq.receive.mqone.JmsReceiver.receiveInMessage(JmsReceiver.java:72)\n" +
                "sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
                "sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n" +
                "sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n" +
                "java.lang.reflect.Method.invoke(Method.java:498)\n" +
                "org.springframework.messaging.handler.invocation.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:180)\n" +
                "org.springframework.messaging.handler.invocation.InvocableHandlerMethod.invoke(InvocableHandlerMethod.java:112)\n" +
                "org.springframework.jms.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:104)\n" +
                "org.springframework.jms.listener.adapter.MessagingMessageListenerAdapter.onMessage(MessagingMessageListenerAdapter.java:69)\n" +
                "org.springframework.jms.listener.AbstractMessageListenerContainer.doInvokeListener(AbstractMessageListenerContainer.java:719)\n" +
                "org.springframework.jms.listener.AbstractMessageListenerContainer.invokeListener(AbstractMessageListenerContainer.java:679)\n" +
                "org.springframework.jms.listener.AbstractMessageListenerContainer.doExecuteListener(AbstractMessageListenerContainer.java:649)\n" +
                "org.springframework.jms.listener.AbstractPollingMessageListenerContainer.doReceiveAndExecute(AbstractPollingMessageListenerContainer.java:317)\n" +
                "org.springframework.jms.listener.AbstractPollingMessageListenerContainer.receiveAndExecute(AbstractPollingMessageListenerContainer.java:255)\n" +
                "org.springframework.jms.listener.DefaultMessageListenerContainer$AsyncMessageListenerInvoker.invokeListener(DefaultMessageListenerContainer.java:1168)\n" +
                "org.springframework.jms.listener.DefaultMessageListenerContainer$AsyncMessageListenerI</Comment>\n" +
                "    </NotifyRequestStatusRequest>\n" +
                "</ns2:NotifyRequestStatusRq>\n";

        System.out.println(new String(str.getBytes("windows-1251"),"UTF-8"));
    }

}
