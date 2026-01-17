package com.shopping.order.FeignClient;

import com.shopping.order.dto.feignDto.ProductFeignResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "product-service",
        url = "${product-service.base-url}"
)
public interface ProductClient {

    @GetMapping("/api/v1/product/{productId}")
    public ProductFeignResponseDto product(@PathVariable Long productId);
}
