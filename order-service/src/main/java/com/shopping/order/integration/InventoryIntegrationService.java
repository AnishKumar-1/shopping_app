package com.shopping.order.integration;

import com.shopping.order.FeignClient.InventoryClient;
import com.shopping.order.dto.inventoryDto.InventoryCheckRequest;
import com.shopping.order.enums.InventoryStatus;
import com.shopping.order.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryIntegrationService {

    private final InventoryClient inventoryClient;


    //inventory client
    @CircuitBreaker(name="inventoryService", fallbackMethod = "inventoryFallBack")
    public InventoryStatus getInventoryClientStatus(Long productId, Integer quantity) {
        return inventoryClient.checkProductAvailability(new InventoryCheckRequest(productId,quantity));
    }

    //inventory fallback method
    public InventoryStatus inventoryFallBack(Long productId, Integer quantity, Throwable ex) {
        log.error("Inventory service failure:", ex);
        throw new ServiceUnavailableException("Inventory service unavailable. Please try again later");
    }

}
