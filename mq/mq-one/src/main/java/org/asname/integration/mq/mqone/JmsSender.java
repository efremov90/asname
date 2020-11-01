package org.asname.integration.mq.mqone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
public class JmsSender {
    /*private JmsTemplate jmsTemplate;
    private Queue queue;

    public void setConnectionFactory(ConnectionFactory cf) {
        this.jmsTemplate = new JmsTemplate(cf);
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public void simpleSend() {
        this.jmsTemplate.send(this.queue, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("hello queue world");
            }
        });
    }*/
    @Autowired
    JmsTemplate jmsTemplate;

    public void sendRequestMessage(final String queueName, final String message) {
        final String textMessage = message;
        System.out.println("Sending message " + textMessage + "to queue - " + queueName);
        jmsTemplate.send(queueName, new MessageCreator() {

            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage(textMessage);
                return message;
            }
        });
    }

    public void sendOutMessage(final String queueName, final String message) {
        final String textMessage = message;
        System.out.println("Sending message " + textMessage + "to queue - " + queueName);
        jmsTemplate.send(queueName, new MessageCreator() {

            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage(textMessage);
                return message;
            }
        });
    }
}
