package com.example.springAMQ.jmsType;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Receiver5 {

    @JmsListener(destination = "mailboxUsingJMSType", containerFactory = "myFactory")
    public void receiveMessage(String email) {
        System.out.println("Received by receiver5 <" + email + "> at - "+ new Date()+" with no selector");
    }

}


