package com.shopping.payment.integration;

import com.shopping.payment.exception.ServiceUnavailableException;
import com.shopping.payment.feignClient.CartClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartIntegrationService {

    private final CartClient cartClient;

    @CircuitBreaker(name="cartService", fallbackMethod = "cartFallBack")
    public void clearCart(){
        cartClient.clear_cart();
    }

    //cart fallback method
    public void cartFallBack(Throwable ex){
        log.error("Cart service error: ",ex);
        throw new ServiceUnavailableException("Cart service unavailable. Please try again later");
    }
}
