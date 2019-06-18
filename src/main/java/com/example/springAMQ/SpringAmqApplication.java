package com.example.springAMQ;

import com.example.springAMQ.compositeQueue.CompositeMessageSender;
import com.example.springAMQ.jmsxgroupId.MessageSender;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;

@SpringBootApplication
@EnableJms
public class SpringAmqApplication {

/*
    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }
*/

    /*@Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }*/


    String BROKER_URL = "failover:(tcp://localhost:61616,tcp://localhost:61626)";
    String BROKER_USERNAME = "admin";
    String BROKER_PASSWORD = "admin";

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(BROKER_URL);
        connectionFactory.setPassword(BROKER_USERNAME);
        connectionFactory.setUserName(BROKER_PASSWORD);
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        return template;
    }

    @Bean
    public DefaultJmsListenerContainerFactory myFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrency("1-4");//sets number of consumers that can parallely process msges from queue
        // follow https://nofluffjuststuff.com/blog/bruce_snyder/2011/08/tuning_jms_message_consumption_in_spring

        return factory;
    }


    public static void main(String[] args) throws JMSException, InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(SpringAmqApplication.class, args);

        //For JMSXGroupID
        MessageSender jmsXGroupIdMessageSender = context.getBean(MessageSender.class);
        jmsXGroupIdMessageSender.sendMessagesUsingJMSXGroupId();

        //For Composite queue
        CompositeMessageSender compMsgSender = context.getBean(CompositeMessageSender.class);
        compMsgSender.sendMessagesUsingCompositeQueue();


    }


}
