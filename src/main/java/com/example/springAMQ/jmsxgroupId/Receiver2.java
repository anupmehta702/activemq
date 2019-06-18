package com.example.springAMQ.jmsxgroupId;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Receiver2 {
    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(String email) {
        System.out.println("Received by receiver2 <" + email + "> at - "+ new Date());
    }

}
