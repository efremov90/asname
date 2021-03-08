package org.asname.integration.kafka.kafkasend;

import org.asname.integration.mq.log.model.DirectionType;
import org.asname.integration.mq.log.model.MQLog;
import org.asname.integration.mq.log.model.StatusType;
import org.asname.integration.mq.log.service.MQLogService;
import org.asname.integration.utils.model.MethodType;
import org.asname.integration.utils.service.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.logging.Logger;

@Component
public class KafkaSender {

    private Logger logger = Logger.getLogger(KafkaSender.class.getName());

    private String Destination;

    public void setDestination(String destination) {
        Destination = destination;
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private void createLogServiceMessageOk(String rqUID, String correlUID, String textMessage,
                                           MethodType methodType, String destination, Exception exception) throws Exception {
        MQLog mqLog = new MQLog();
        mqLog.setRqUID(rqUID);
        mqLog.setCorrelationUID(correlUID);
        mqLog.setCreateDatetime(new Date());
        mqLog.setDirection(DirectionType.OUT);
        mqLog.setContent(textMessage);
        mqLog.setMethod(methodType);
        mqLog.setStatus(StatusType.OK);
        mqLog.setDestination(destination);
        mqLog.setError(exception != null ? new IntegrationService().getExceptionString(exception) : null);
        new MQLogService().create(mqLog);
    }

    public void send(String rqUID, String correlUID, final String textMessage, MethodType methodType, Exception exception) throws Exception {
        kafkaTemplate.send("OUT", textMessage);
        createLogServiceMessageOk(rqUID, correlUID, textMessage, methodType, "topic://"+Destination, exception);
    }
}
