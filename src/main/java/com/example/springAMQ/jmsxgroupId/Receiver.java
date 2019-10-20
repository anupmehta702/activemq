package com.example.springAMQ.jmsxgroupId;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Receiver {
    private int consumerNumber = 1;

    /*@JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(String email) throws InterruptedException {
        System.out.println("Received by receiver0  <" + email + "> received at - " + new Date());
        if (email.contains("yahoo")) {
            System.out.println("Pausing Yahoo consumer for 20 seconds");
            Thread.sleep(20000);
        }

    }
*/
    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessageWithHeader(Message<String> message) throws InterruptedException {
        MessageHeaders headers=message.getHeaders();
        System.out.println("Received by receiver0  <" + message.getPayload() + "> received at - " + new Date());
        System.out.println("JMSXGroupId --> "+headers.get("JMSXGroupID").toString()+" Id --> "+headers.get("jms_messageId").toString());
        if (headers.get("JMSXGroupID").toString().equalsIgnoreCase("yahoo")) {
            System.out.println("Pausing Yahoo consumer for 20 seconds");
            Thread.sleep(20000);
        }
        //System.out.println("Printing headers--"+headers);
        if(headers.containsKey("JMSXGroupFirstForConsumer")){
            System.out.println("Group changed");
        }

    }

}
