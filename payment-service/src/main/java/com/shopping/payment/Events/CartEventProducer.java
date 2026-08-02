package com.shopping.payment.Events;

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
        kafkaTemplate.send("cart-clear-topic",event.orderId().toString(),event)
                .whenComplete((result, e) -> {
                    if(e==null){
                        log.info("Event published. eventId={}, topic={}, partition={}, offset={}",
                                event.eventId(),
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }else{
                        log.error("Failed to publish payment event. eventId={}", event.eventId(),e);
                    }
                });

    }
}
