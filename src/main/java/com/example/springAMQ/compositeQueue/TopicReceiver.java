package com.example.springAMQ.compositeQueue;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TopicReceiver {
    @JmsListener(destination = "BAR", containerFactory = "myFactory")
    public void receiveMessage(String msg) {
        System.out.println("Received by Topic bar  <" + msg + ">");
    }

}
