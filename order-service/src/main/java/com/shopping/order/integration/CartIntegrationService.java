package com.shopping.order.integration;

import com.shopping.order.FeignClient.CartClient;
import com.shopping.order.dto.cartDto.CartResponse;
import com.shopping.order.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartIntegrationService {

    private final CartClient cartClient;

    //Cart client
    @CircuitBreaker(name="cartService",fallbackMethod = "cartFallBack")
    public CartResponse cartClientFeignResponse(){
        return cartClient.get_cart();
    }

    public CartResponse cartFallBack(Throwable ex){
        log.error("Cart service failure:", ex);
        throw new ServiceUnavailableException("Cart service unavailable. Please try again later");
    }
}
