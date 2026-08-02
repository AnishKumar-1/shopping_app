package com.shopping.cart.feignClient;

import com.shopping.cart.dto.CartItemsDto;
import com.shopping.cart.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/api/v1/products/{id}/product")
    ProductResponse getProduct(@PathVariable Long id);

}
