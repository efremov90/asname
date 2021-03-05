package org.asname.integration.kafkaone;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.integration.contract.requests.kafkaone.*;
import org.asname.integration.mq.log.model.DirectionType;
import org.asname.integration.mq.log.model.MQLog;
import org.asname.integration.mq.log.model.StatusType;
import org.asname.integration.mq.log.service.MQLogService;
import org.asname.integration.utils.model.MethodType;
import org.asname.integration.utils.service.IntegrationService;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class KafkaReceiver {

    private Logger logger = Logger.getLogger(KafkaReceiver.class.getName());

    private void createLogServiceMessageOk (Header baseType, String textMessage, MethodType methodType,
                                            String destination) throws Exception {

        new MQLogService().validateExistsMessage(baseType.getRqUID());

        MQLog mqLog = new MQLog();
        mqLog.setRqUID(baseType.getRqUID());
        mqLog.setCreateDatetime(new Date());
        mqLog.setDirection(DirectionType.IN);
        mqLog.setContent(textMessage);
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

    @KafkaListener(topics = "IN")
    public void receive(String message) throws Exception {
        String Destination = "topic://IN";
        logger.info("start");
        Header baseType;
        MethodType methodType = MethodType.Unknown;
            try {
                methodType = new IntegrationService().getMethod(message);
                InputStream inputStream = getClass().getResourceAsStream(RequestServiceImpl.pathSchema);
                JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
                Schema schema = SchemaLoader.load(rawSchema);
                schema.validate(new JSONObject(message));
                ObjectMapper mapper = new ObjectMapper();
                Requests requests = mapper.readValue(message, Requests.class);
                if (requests.getCreateRequestRq()!=null) {
                    CreateRequestRq createRequestRq = requests.getCreateRequestRq();
                    baseType = createRequestRq.getHeader();
                    createLogServiceMessageOk(baseType,message,methodType,Destination);
                    new RequestServiceImpl().createRequestRq(createRequestRq);
                } else if (requests.getCancelRequestRq()!=null) {
                    CancelRequestRq cancelRequestRq = requests.getCancelRequestRq();
                    baseType = cancelRequestRq.getHeader();
                    createLogServiceMessageOk(baseType,message,methodType,Destination);
                    new RequestServiceImpl().cancelRequestRq(cancelRequestRq);
                } else {
                    throw new Exception("Неизвестный тип сообщения");
                }
            } catch (Exception e) {

                e.printStackTrace();

                createLogServiceMessageError(message,
                        methodType,
                        new IntegrationService().getExceptionString(e),
                        Destination);
            }
    }
}
