package com.shopping.order.integration;

import com.shopping.order.FeignClient.ProductClient;
import com.shopping.order.dto.feignDto.ProductFeignResponseDto;
import com.shopping.order.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductIntegrationService {

    private final ProductClient productClient;

    //product client call
    @CircuitBreaker(name="productService", fallbackMethod = "productFallBack")
    public ProductFeignResponseDto getProductFeignResponse(Long productId){
        return  productClient.product(productId);
    }

    //product fallback method
    public ProductFeignResponseDto  productFallBack(Long productId,Throwable ex){
        log.error("Product service failure",ex);
        throw new ServiceUnavailableException("Product service unavailable. Please try again later");
    }
}
