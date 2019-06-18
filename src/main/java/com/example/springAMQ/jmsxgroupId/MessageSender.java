package com.example.springAMQ.jmsxgroupId;

import com.example.springAMQ.SpringAmqApplication;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Date;

@Component
public class MessageSender {

    @Autowired
    JmsTemplate jmsTemplate;

    public MessageSender() {
    }

    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public  void sendMessagesUsingJMSXGroupId() throws JMSException, InterruptedException {
        jmsTemplate.convertAndSend("mailbox", getActiveMQTextMessage("gmail"));
        jmsTemplate.convertAndSend("mailbox", getActiveMQTextMessage("yahoo"));
        jmsTemplate.convertAndSend("mailbox", getActiveMQTextMessage("yahoo"));
        jmsTemplate.convertAndSend("mailbox", getActiveMQTextMessage("gmail"));
        jmsTemplate.convertAndSend("mailbox", getActiveMQTextMessage("microsoft"));
        jmsTemplate.convertAndSend("mailbox", getActiveMQTextMessage("microsoft"));
        jmsTemplate.convertAndSend("mailbox", getActiveMQTextMessage("yahoo"));
        jmsTemplate.convertAndSend("mailbox", getActiveMQTextMessage("facebook"));
        jmsTemplate.convertAndSend("mailbox", getActiveMQTextMessage("facebook"));
    }

    private static ActiveMQTextMessage getActiveMQTextMessage(String type) throws JMSException, InterruptedException {
        ActiveMQTextMessage message = new ActiveMQTextMessage();
        message.setText("Msg from "+type+" sent at "+new Date());
        //Thread.sleep(2000);
        message.setStringProperty("JMSXGroupID", type);
        return message;
    }

}
