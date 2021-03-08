package org.asname.integration.mq.send;

public class Test {
  public static void main(String[] args) {
    JmsSender jmsSender = new JmsSender();
    jmsSender.setJmsTemplate(JmsConfig.getJmsTemplate());
//    jmsSender.sendMessage("1","2","text", MethodType.NotifyRequestStatusRq);
  }
}
