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
public class MessageProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessageToDestination(String queue, final String messagePost) {
        jmsTemplate.send(queue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message1 = session.createObjectMessage(messagePost);
                return message1;
            }
        });
    }

}