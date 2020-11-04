package org.asname.integration.mq.mqone;

import org.asname.integration.mq.MessageConverter;
import org.asname.integration.requests.mq.*;
import org.asname.model.integration.*;
import org.asname.model.integration.StatusType;
import org.asname.service.integration.IntegrationService;
import org.asname.service.integration.MQLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Date;

import static org.asname.model.integration.MethodType.Unknown;

@Component
public class JmsReceiver {
    @Autowired
    private JmsSender producer;
    private final static String pathSchema = "core/src/main/java/org/asname/integration/requests/mq/Requests.xsd";

    @JmsListener(destination = "AS1.IN")
    public void receiveInMessage(final Message message) throws Exception {
        String inTextMessage = null;
        BaseType baseType;
        if (message instanceof TextMessage) {
            try {
                inTextMessage = ((TextMessage) message).getText();
                MethodType methodType = new IntegrationService().getMethod(inTextMessage);
                switch (methodType) {
                    case CreateRequestRq: {
                        CreateRequestRqType createRequestRq = (CreateRequestRqType) new MessageConverter(new CreateRequestRqType(),
                                pathSchema).unmarshal(inTextMessage);
                        baseType = (BaseType) createRequestRq;

                        MQLog mqLog = new MQLog();
                        mqLog.setRqUID(baseType.getRqUID());
                        mqLog.setCreateDatetime(new Date());
                        mqLog.setDirection(DirectionType.IN);
                        mqLog.setContent(new IntegrationService().transformXML(inTextMessage));
                        mqLog.setMethod(methodType);
                        mqLog.setStatus(StatusType.OK);
                        new MQLogService().create(mqLog);

                        new RequestServiceImpl().createRequest(createRequestRq);
                        break;
                    }
                    case CancelRequestRq: {
                        CancelRequestRqType cancelRequestRq =
                                (CancelRequestRqType) new MessageConverter(new CancelRequestRqType(),
                                        pathSchema).unmarshal(inTextMessage);
                        baseType = (BaseType) cancelRequestRq;

                        MQLog mqLog = new MQLog();
                        mqLog.setRqUID(baseType.getRqUID());
                        mqLog.setCreateDatetime(new Date());
                        mqLog.setDirection(DirectionType.IN);
                        mqLog.setContent(new IntegrationService().transformXML(inTextMessage));
                        mqLog.setMethod(methodType);
                        mqLog.setStatus(StatusType.OK);
                        new MQLogService().create(mqLog);

                        new RequestServiceImpl().cancelRequest(cancelRequestRq);
                        break;
                    }
                    default:
                        throw new Exception("Неизвестный тип сообщения");
                }
            } catch (Exception e) {

                e.printStackTrace();

                MQLog mqLog = new MQLog();
                mqLog.setCreateDatetime(new Date());
                mqLog.setDirection(DirectionType.IN);
                mqLog.setContent(new IntegrationService().transformXML(inTextMessage));
                mqLog.setMethod(Unknown);
                mqLog.setStatus(StatusType.OK);
                new MQLogService().create(mqLog);
            }
        } else {
            throw new IllegalArgumentException("Message must be of type TextMessage");
        }
    }

    @JmsListener(destination = "AS1.RESPONSE")
    public void receiveResponseMessage(final Message message) throws Exception {
        String inTextMessage = null;
        BaseType baseType;
        if (message instanceof TextMessage) {
            try {
                inTextMessage = ((TextMessage) message).getText();
                MethodType methodType = new IntegrationService().getMethod(inTextMessage);
                switch (methodType) {
                    case CreateRequestRs: {
                        CreateRequestRsType createRequestRs =
                                (CreateRequestRsType) new MessageConverter(new CreateRequestRsType(),
                                pathSchema).unmarshal(inTextMessage);
                        baseType = (BaseType) createRequestRs;

                        MQLog mqLog = new MQLog();
                        mqLog.setRqUID(baseType.getRqUID());
                        mqLog.setCreateDatetime(new Date());
                        mqLog.setDirection(DirectionType.IN);
                        mqLog.setContent(new IntegrationService().transformXML(inTextMessage));
                        mqLog.setMethod(methodType);
                        mqLog.setStatus(StatusType.OK);
                        new MQLogService().create(mqLog);

                        break;
                    }
                    case CancelRequestRs: {
                        CancelRequestRsType cancelRequestRs =
                                (CancelRequestRsType) new MessageConverter(new CancelRequestRsType(),
                                        pathSchema).unmarshal(inTextMessage);
                        baseType = (BaseType) cancelRequestRs;

                        MQLog mqLog = new MQLog();
                        mqLog.setRqUID(baseType.getRqUID());
                        mqLog.setCreateDatetime(new Date());
                        mqLog.setDirection(DirectionType.IN);
                        mqLog.setContent(new IntegrationService().transformXML(inTextMessage));
                        mqLog.setMethod(methodType);
                        mqLog.setStatus(StatusType.OK);
                        new MQLogService().create(mqLog);

                        break;
                    }
                    default:
                        throw new Exception("Неизвестный тип сообщения");
                }
            } catch (Exception e) {

                e.printStackTrace();

                MQLog mqLog = new MQLog();
                mqLog.setCreateDatetime(new Date());
                mqLog.setDirection(DirectionType.IN);
                mqLog.setContent(new IntegrationService().transformXML(inTextMessage));
                mqLog.setMethod(Unknown);
                mqLog.setStatus(StatusType.OK);
                new MQLogService().create(mqLog);
            }
        } else {
            throw new IllegalArgumentException("Message must be of type TextMessage");
        }
    }
}
