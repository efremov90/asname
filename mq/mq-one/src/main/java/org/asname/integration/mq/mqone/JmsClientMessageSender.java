package org.asname.integration.mq.mqone;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Date;

public class JmsClientMessageSender {

    public static void main(String[] args) {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616"); // ActiveMQ-specific
        Connection con = null;
        try {
            con = factory.createConnection();
            Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE); // non-transacted session

            Queue queue = session.createQueue("AS1.REQUEST"); // only specifies queue name

            MessageProducer producer = session.createProducer(queue);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            producer.setPriority(0);
            producer.setTimeToLive(100000);

            Message msg = session.createTextMessage("message: "+new Date().toString()); // text message
            msg.setJMSCorrelationID("923-923");
            producer.send(msg);

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close(); // free all resources
                } catch (JMSException e) { /* Ignore */ }
            }
        }
    }
}