package br.com.compass.pb.blogpass.config;

import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.ErrorHandler;

@Configuration
@EnableJms
public class JmsConfig {

    public JmsListenerContainerFactory<?> myFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        // anonymous class
        factory.setErrorHandler(
                new ErrorHandler() {
                    @Override
                    public void handleError(Throwable t) {
                        System.err.println("An error has occurred in the transaction");
                    }
                });

        factory.setErrorHandler(t -> System.err.println("An error has occurred in the transaction"));

        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
