package org.asname.integration.mq.receive.mqone;

import org.asname.integration.contract.requests.mq.*;
import org.asname.integration.mq.MessageConverter;
import org.asname.integration.mq.log.model.DirectionType;
import org.asname.integration.mq.log.model.MQLog;
import org.asname.integration.mq.log.model.StatusType;
import org.asname.integration.mq.log.service.MQLogService;
import org.asname.integration.utils.model.MethodType;
import org.asname.integration.utils.service.IntegrationService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JmsReceiver {

    private Logger logger = Logger.getLogger(JmsReceiver.class.getName());

    private void createLogServiceMessageOk (BaseType baseType, String textMessage, MethodType methodType,
                                            String destination) throws Exception {

        new MQLogService().validateExistsMessage(baseType.getRqUID());

        MQLog mqLog = new MQLog();
        mqLog.setRqUID(baseType.getRqUID());
        mqLog.setCreateDatetime(new Date());
        mqLog.setDirection(DirectionType.IN);
        mqLog.setContent(new IntegrationService().transformXML(textMessage));
        mqLog.setMethod(methodType);
        mqLog.setStatus(StatusType.OK);
        mqLog.setDestination(destination);
        new MQLogService().create(mqLog);
    }

    private void createLogServiceMessageError (String textMessage, MethodType methodType, String error,
                                               String destination) throws Exception {
        MQLog mqLog = new MQLog();
        mqLog.setCreateDatetime(new Date());
        mqLog.setDirection(DirectionType.IN);
        mqLog.setContent(textMessage);
        mqLog.setMethod(methodType);
        mqLog.setStatus(StatusType.ERROR);
        mqLog.setDestination(destination);
        mqLog.setError(error);
        new MQLogService().create(mqLog);
    }

    @JmsListener(destination = "AS1.IN")
    public void receiveInMessage(final Message message) throws Exception {
        logger.info("start");
//        System.out.println(getClass().getResource("/Requests.xsd"));
        String textMessage = null;
        BaseType baseType;
        MethodType methodType = MethodType.Unknown;
        if (message instanceof TextMessage) {
            try {
                textMessage = ((TextMessage) message).getText();
                methodType = new IntegrationService().getMethod(textMessage);
                switch (methodType) {
                    case CreateRequestRq: {
                        CreateRequestRqType createRequestRq = (CreateRequestRqType) new MessageConverter(new CreateRequestRqType(),
                                RequestServiceImpl.pathSchema).unmarshal(textMessage);
                        baseType = (BaseType) createRequestRq;

                        createLogServiceMessageOk(baseType,textMessage,methodType,message.getJMSDestination().toString());

                        new RequestServiceImpl().createRequestRq(createRequestRq);
                        break;
                    }
                    case CancelRequestRq: {
                        CancelRequestRqType cancelRequestRq =
                                (CancelRequestRqType) new MessageConverter(new CancelRequestRqType(),
                                        RequestServiceImpl.pathSchema).unmarshal(textMessage);
                        baseType = (BaseType) cancelRequestRq;

                        createLogServiceMessageOk(baseType,textMessage,methodType,message.getJMSDestination().toString());

                        new RequestServiceImpl().cancelRequestRq(cancelRequestRq);
                        break;
                    }
                    default:
                        throw new Exception("Неизвестный тип сообщения");
                }
            } catch (Exception e) {

                e.printStackTrace();

                createLogServiceMessageError(textMessage,
                        methodType,
                        new IntegrationService().getExceptionString(e),
                        message.getJMSDestination().toString());
            }
        } else {
            throw new IllegalArgumentException("Message must be of type TextMessage");
        }
    }

    @JmsListener(destination = "AS1.RESPONSE")
    public void receiveResponseMessage(final Message message) throws Exception {
        logger.info("start");
        String textMessage = null;
        BaseType baseType;
        MethodType methodType = MethodType.Unknown;
        if (message instanceof TextMessage) {
            try {
                textMessage = ((TextMessage) message).getText();
                methodType = new IntegrationService().getMethod(textMessage);
                switch (methodType) {
                    case CreateRequestRs: {
                        CreateRequestRsType createRequestRs =
                                (CreateRequestRsType) new MessageConverter(new CreateRequestRsType(),
                                RequestServiceImpl.pathSchema).unmarshal(textMessage);
                        baseType = (BaseType) createRequestRs;

                        createLogServiceMessageOk(baseType,textMessage,methodType,message.getJMSDestination().toString());

                        break;
                    }
                    case CancelRequestRs: {
                        CancelRequestRsType cancelRequestRs =
                                (CancelRequestRsType) new MessageConverter(new CancelRequestRsType(),
                                        RequestServiceImpl.pathSchema).unmarshal(textMessage);
                        baseType = (BaseType) cancelRequestRs;

                        createLogServiceMessageOk(baseType,textMessage,methodType,message.getJMSDestination().toString());

                        break;
                    }
                    default:
                        throw new Exception("Неизвестный тип сообщения");
                }
            } catch (Exception e) {

                e.printStackTrace();

                createLogServiceMessageError(textMessage,
                        methodType,
                        new IntegrationService().getExceptionString(e),
                        message.getJMSDestination().toString());
            }
        } else {
            throw new IllegalArgumentException("Message must be of type TextMessage");
        }
    }
}
