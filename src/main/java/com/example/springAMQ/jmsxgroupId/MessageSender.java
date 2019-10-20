package com.example.springAMQ.jmsxgroupId;

import com.example.springAMQ.SpringAmqApplication;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MessageSender {

    @Autowired
    JmsTemplate jmsTemplate;

    int counter=1;

    public MessageSender() {
    }

    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }


    public  void sendMessagesUsingJMSType() throws JMSException, InterruptedException {
        jmsTemplate.convertAndSend("mailboxUsingJMSType", getActiveMQTextMessageUsingJMSType("gmail"));
        jmsTemplate.convertAndSend("mailboxUsingJMSType", getActiveMQTextMessageUsingJMSType("yahoo"));
        jmsTemplate.convertAndSend("mailboxUsingJMSType", getActiveMQTextMessageUsingJMSType("yahoo"));
        jmsTemplate.convertAndSend("mailboxUsingJMSType", getActiveMQTextMessageUsingJMSType("gmail"));
        jmsTemplate.convertAndSend("mailboxUsingJMSType", getActiveMQTextMessageUsingJMSType("yahoo"));
        jmsTemplate.convertAndSend("mailboxUsingJMSType", getActiveMQTextMessageUsingJMSType("facebook"));
        jmsTemplate.convertAndSend("mailboxUsingJMSType", getActiveMQTextMessageUsingJMSType("facebook"));
        jmsTemplate.convertAndSend("mailboxUsingJMSType", getActiveMQTextMessageUsingJMSType("outlook"));
        jmsTemplate.convertAndSend("mailboxUsingJMSType", getActiveMQTextMessageUsingJMSType("outlook"));

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

    private  ActiveMQTextMessage getActiveMQTextMessageUsingJMSType(String type) throws JMSException, InterruptedException {
        ActiveMQTextMessage message = new ActiveMQTextMessage();
        message.setText("Msg from "+type+" sent at "+new Date());
        if(type.equalsIgnoreCase("yahoo")) {
            message.setJMSType("YahooMessage");
        }else if(type.equalsIgnoreCase("gmail")) {
            message.setJMSType("GmailMessage");
        }
        message.setStringProperty("JMSXGroupID", type);
        return message;
    }


    private  ActiveMQTextMessage getActiveMQTextMessage(String type) throws JMSException, InterruptedException {
        ActiveMQTextMessage message = new ActiveMQTextMessage();

        message.setText("Msg from "+type+" sent at "+new Date());
        /*if(type.equalsIgnoreCase("yahoo") && counter ==3 ){
            System.out.println("Deliberately setting group seq to -1");
            message.setIntProperty("JMSXGroupSeq", -1);
        }*/
        message.setStringProperty("JMSXGroupID", type);
        counter++;
        return message;
    }

}
