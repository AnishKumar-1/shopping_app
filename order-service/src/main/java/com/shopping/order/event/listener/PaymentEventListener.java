package com.shopping.order.event.listener;

import com.shopping.order.records.PaymentResultEvent;
import com.shopping.order.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final OrderService orderService;

//    @RetryableTopic(attempts = "3")
    @KafkaListener(
            topics = "payment-result",
            groupId = "order-service-v2"
    )
    public void consume(PaymentResultEvent event, Acknowledgment ack) {
      log.info("Received PaymentResultEvent. eventId={}", event.eventId());
        orderService.handlePaymentResult(event);
        ack.acknowledge();
    }

//    @DltHandler
//    public void handleDlt(PaymentResultEvent event) {
//        log.error("Received in DLT : {}", event);
//    }


}
