package com.example.springAMQ.jmsType;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Receiver3 {

    @JmsListener(destination = "mailboxUsingJMSType", containerFactory = "myFactory",selector = "JMSType='YahooMessage'")
    public void receiveMessage(String email) {
        System.out.println("Received by receiver3 <" + email + "> at - "+ new Date()+" with selector - YahooMessage");
    }

}
