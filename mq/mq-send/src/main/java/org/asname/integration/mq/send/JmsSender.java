package org.asname.integration.mq.send;

import org.asname.integration.mq.log.model.DirectionType;
import org.asname.integration.mq.log.model.MQLog;
import org.asname.integration.mq.log.model.StatusType;
import org.asname.integration.mq.log.service.MQLogService;
import org.asname.integration.utils.model.MethodType;
import org.asname.integration.utils.service.IntegrationService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.logging.Logger;

//@Component
public class JmsSender {

    private Logger logger = Logger.getLogger(JmsSender.class.getName());

//    @Autowired
    private JmsTemplate jmsTemplate;

    private String Destination;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    private void createLogServiceMessageOk(String rqUID, String correlUID, String textMessage,
                                           MethodType methodType) throws Exception {
        MQLog mqLog = new MQLog();
        mqLog.setRqUID(rqUID);
        mqLog.setCorrelationUID(correlUID);
        mqLog.setCreateDatetime(new Date());
        mqLog.setDirection(DirectionType.OUT);
        mqLog.setContent(new IntegrationService().transformXML(textMessage));
        mqLog.setMethod(methodType);
        mqLog.setStatus(StatusType.OK);
        new MQLogService().create(mqLog);
    }

    public void sendMessage(String rqUID, String correlUID, final String textMessage, MethodType methodType) {
        logger.info("start");

        try {
            jmsTemplate.send(Destination, new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(textMessage);
                }
            });

            createLogServiceMessageOk(rqUID, correlUID, textMessage, methodType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
