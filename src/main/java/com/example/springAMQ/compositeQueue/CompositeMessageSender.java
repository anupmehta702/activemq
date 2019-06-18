package com.example.springAMQ.compositeQueue;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Date;

@Component
public class CompositeMessageSender {

    @Autowired
    JmsTemplate jmsTemplate;

    public CompositeMessageSender() {
    }

    public CompositeMessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessagesUsingCompositeQueue() throws JMSException, InterruptedException {
        jmsTemplate.convertAndSend("MY.QUEUE", getActiveMQTextMessage());
    }

    private static ActiveMQTextMessage getActiveMQTextMessage() throws JMSException, InterruptedException {
        ActiveMQTextMessage message = new ActiveMQTextMessage();
        message.setText("Msg from MY.QUEUE delivered at " + new Date().getSeconds());
        return message;
    }

}
