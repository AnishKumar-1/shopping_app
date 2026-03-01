package com.shopping.payment.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;

@FeignClient(name="cart-service")
public interface CartClient {

    @DeleteMapping("/api/v1/cart")
    void clear_cart();
}
