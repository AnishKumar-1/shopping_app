package com.shopping.order.FeignClient;

import com.shopping.order.dto.inventoryDto.InventoryCheckRequest;
import com.shopping.order.dto.inventoryDto.InventoryCheckResponse;
import com.shopping.order.dto.inventoryDto.ReserveRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="inventory-service", url = "${inventory-service.base-url}")
public interface InventoryClient {

    @PostMapping("/api/v1/inventories/check")
    InventoryCheckResponse checkProductAvailability(@RequestBody InventoryCheckRequest request);

    @PostMapping("/api/v1/inventories/{product_id}/reserve")
    public ReserveRequestDto reserve(@PathVariable Long product_id, @RequestParam Integer quantity);
}
