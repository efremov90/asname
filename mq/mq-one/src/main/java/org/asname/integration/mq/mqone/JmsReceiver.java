package org.asname.integration.mq.mqone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
public class JmsReceiver {
    /*@JmsListener(destination = "AS1.IN")
    public void receiveRequest(Message message) throws JMSException {
        System.out.println(message.getJMSMessageID());
    }
    @JmsListener(destination = "AS1.RESPONSE")
    public void receiveResponse(Message message) throws JMSException {
        System.out.println(message.getJMSMessageID());
    }*/
    @Autowired
    private JmsSender producer;

    @JmsListener(destination = "AS1.IN")
    public void receiveInMessage(final Message textMessage) throws JMSException {
        String messageData = textMessage.toString();
        System.out.println("Received message getJMSMessageID" + textMessage.getJMSMessageID());
        System.out.println("Received message getJMSCorrelationID" + textMessage.getJMSCorrelationID());
        System.out.println("Received message getJMSType" + textMessage.getJMSType());
        System.out.println("Received message toString" + textMessage.toString());
        if (textMessage instanceof TextMessage) {
            try {
                System.out.println("Received message textMessage" + ((TextMessage) textMessage).getText());
            }
            catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
        }
//        producer.sendOutMessage("AS1.OUT", messageData);
    }
    @JmsListener(destination = "AS1.RESPONSE")
    public void receiveResponseMessage(final Message jsonMessage) throws JMSException {
        String messageData = jsonMessage.toString();
        System.out.println("Received message " + jsonMessage);
    }
}
