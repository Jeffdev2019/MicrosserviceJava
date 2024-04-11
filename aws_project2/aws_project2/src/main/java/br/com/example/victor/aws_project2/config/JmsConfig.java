package br.com.example.victor.aws_project2.config;


import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.Session;

@Configuration
@EnableJms
@Profile("!debug")
public class JmsConfig {

    private static final Logger log = LoggerFactory.getLogger(
            JmsConfig.class);
    @Value("${aws.region}")
    private String awsRegion;

    private SQSConnectionFactory sqsConnectionFactory;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
       try{
           sqsConnectionFactory = new SQSConnectionFactory(
                   new ProviderConfiguration(),
                   AmazonSQSClientBuilder.standard()
                           .withRegion(awsRegion)
                           .withCredentials(new DefaultAWSCredentialsProviderChain())
                           .build()
           );

           DefaultJmsListenerContainerFactory factory =
                   new DefaultJmsListenerContainerFactory();
           factory.setConnectionFactory(sqsConnectionFactory);
           factory.setDestinationResolver(new DynamicDestinationResolver());
           factory.setConcurrency("2");
           factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);

           return factory;
       }
       catch (Exception e){
           log.error("Ocorreu um erro inesperado! \n" + e.getMessage());
           return new DefaultJmsListenerContainerFactory();
       }

    }
}
