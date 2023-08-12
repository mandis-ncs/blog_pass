package br.com.compass.pb.blogpass.messaging.producers;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class MessageProducerComment {

    @Autowired
    private JmsTemplate jmsTemplate;


    public void sendMessageToDestination(String queue, final String messageComment) {
        jmsTemplate.send(queue, session -> session.createObjectMessage(messageComment));
    }

}