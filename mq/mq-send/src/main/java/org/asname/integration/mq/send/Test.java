package org.asname.integration.mq.send;

import org.asname.integration.utils.model.MethodType;

public class Test {
  public static void main(String[] args) {
    JmsSender jmsSender = new JmsSender();
    jmsSender.setJmsTemplate(JmsConfigASNAME1.getJmsTemplate());
//    jmsSender.sendMessage("1","2","text", MethodType.NotifyRequestStatusRq);
  }
}
