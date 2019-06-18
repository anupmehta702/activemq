package com.example.springAMQ.compositeQueue;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class CompositeQueueReceiver {
    @JmsListener(destination = "FOO", containerFactory = "myFactory")
    public void receiveMessage(String email) {
        System.out.println("Received by CompositeQueueReceiver foo queue  <" + email + ">");
    }

}
