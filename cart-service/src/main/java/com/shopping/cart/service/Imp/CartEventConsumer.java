package com.shopping.cart.service.Imp;

import com.shopping.cart.dto.CartClearEvent;
import com.shopping.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartEventConsumer {

    private final CartService cartService;
    public static final String CART_CLEAR_TOPIC = "cart-clear-topic";

    @KafkaListener(topics = CART_CLEAR_TOPIC, groupId = "cart-group")
    public void consume(CartClearEvent event) {
        try {
            log.info("Received event for order: {}", event.getOrderId());
            cartService.clear_cart();
        } catch (Exception e) {
            log.error("Error processing cart clear event for order: {}", event.getOrderId(), e);
            throw e; // important for retry (later use)
        }
    }
}
