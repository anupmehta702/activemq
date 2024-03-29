package com.example.springAMQ.jmsType;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Receiver4 {

    @JmsListener(destination = "mailboxUsingJMSType", containerFactory = "myFactory",selector = "JMSType='GmailMessage'")
    public void receiveMessage(String email) {
        System.out.println("Received by receiver4 <" + email + "> at - "+ new Date()+" with selector - GmailMessage");
    }

}


