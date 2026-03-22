package com.shopping.payment.services;

import com.shopping.payment.dto.CartClearEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartEventProducer {

    @Autowired
    private KafkaTemplate<String, CartClearEvent> kafkaTemplate;

    public void sendCartClearEvent(CartClearEvent event){
        kafkaTemplate.send("cart-clear-topic",event)
                .whenComplete((result,ex)->{
                    if(ex==null){
                        log.info("Message sent: {}", event.getOrderId());
                    }else{
                        log.error("Failed to send message: {}", ex.getMessage());
                    }
                });

    }
}
