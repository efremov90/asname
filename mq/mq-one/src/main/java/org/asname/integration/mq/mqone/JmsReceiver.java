package org.asname.integration.mq.mqone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
public class JmsReceiver /*implements MessageListener*/ {
    /*public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                System.out.println(((TextMessage) message).getText());
            }
            catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            throw new IllegalArgumentException("Message must be of type TextMessage");
        }
    }*/
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
    public void receiveMessage(final Message jsonMessage) throws JMSException {
        String messageData = jsonMessage.toString();
        System.out.println("Received message " + jsonMessage);
        producer.sendMessage("AS1.OUT", messageData);
    }
}
