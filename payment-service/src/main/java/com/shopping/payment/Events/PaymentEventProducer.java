package com.shopping.payment.Events;

import com.shopping.payment.records.PaymentResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentResultEvent> kafkaTemplate;

    public void sendPaymentResult(PaymentResultEvent event){
        kafkaTemplate.send(
                "payment-result",event.orderId().toString(),event
        ).whenComplete((result, e) -> {
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
