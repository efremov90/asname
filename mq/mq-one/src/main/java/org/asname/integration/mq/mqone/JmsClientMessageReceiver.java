package org.asname.integration.mq.mqone;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsClientMessageReceiver {

    public static void main(String[] args) {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616"); // ActiveMQ-specific
        Connection con = null;

        try {
            con = factory.createConnection();
            Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE); // non-transacted session

            Queue queue = session.createQueue("AS1.IN"); // only specifies queue name

            MessageConsumer consumer = session.createConsumer(queue);

            con.start(); // start the connection
            while (true) { // run forever
                Message msg = consumer.receive(); // blocking!
                if (!(msg instanceof TextMessage))
                    throw new RuntimeException("Expected a TextMessage");
                TextMessage tm = (TextMessage) msg;
                System.out.println(tm.getText()); // print message content
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (JMSException e) {/* Ignore */ }
        }
    }
}