package org.asname.integration.mq.send;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

public class JmsConfigASNAME1 {

    private static JmsTemplate jmsTemplate;


    private static String BROKER_URL = "tcp://localhost:61616";
    private static String BROKER_USERNAME = "admin";
    private static String BROKER_PASSWORD = "admin";

    private static ActiveMQConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(BROKER_URL);
        connectionFactory.setPassword(BROKER_USERNAME);
        connectionFactory.setUserName(BROKER_PASSWORD);
        return connectionFactory;
    }

    private static JmsTemplate jmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        return template;
    }

    public static JmsTemplate getJmsTemplate() {
        if (jmsTemplate != null) {
            return jmsTemplate;
        } else {
            return jmsTemplate();
        }

    }
}
