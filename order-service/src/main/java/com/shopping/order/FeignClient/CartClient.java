package com.shopping.order.FeignClient;

import com.shopping.order.dto.cartDto.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="cart-service")
public interface CartClient {

    @DeleteMapping("/api/v1/cart")
    void clear_cart();
    @GetMapping("/api/v1/cart")
    CartResponse get_cart();
}
