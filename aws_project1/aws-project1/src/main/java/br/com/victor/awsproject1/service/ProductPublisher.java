package br.com.victor.awsproject1.service;

import br.com.victor.awsproject1.Enums.EventType;
import br.com.victor.awsproject1.model.Envelope;
import br.com.victor.awsproject1.model.Product;
import br.com.victor.awsproject1.model.ProductEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.Topic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProductPublisher {
    private static final Logger log = LoggerFactory.getLogger(
            ProductPublisher.class);
    private final AmazonSNS snsClient;
    private final Topic productEventsTopic;
    private final ObjectMapper objectMapper;

    public ProductPublisher(AmazonSNS snsClient,
                            @Qualifier("productEventsTopic") Topic productEventsTopic,
                            ObjectMapper objectMapper){

        this.snsClient = snsClient;
        this.productEventsTopic = productEventsTopic;
        this.objectMapper = objectMapper;
    }


    public void publishProductEvent(Product product, EventType eventType, String userName){
        ProductEvent productEvent = new ProductEvent();
        productEvent.setProductId(product.getID());
        productEvent.setCode(product.getCode());
        productEvent.setUserName(userName);

        Envelope envelope = new Envelope();
        envelope.setEventType(eventType);

        try {
            envelope.setData(objectMapper.writeValueAsString(productEvent));

            snsClient.publish(
                    productEventsTopic.getTopicArn(),
                    objectMapper.writeValueAsString(envelope));

        } catch (JsonProcessingException e) {
            log.error("Failed to create product event message");
        }


    }




}
