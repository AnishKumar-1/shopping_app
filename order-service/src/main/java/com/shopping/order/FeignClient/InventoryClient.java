package com.shopping.order.FeignClient;

import com.shopping.order.dto.inventoryDto.InventoryActionRequest;
import com.shopping.order.dto.inventoryDto.InventoryCheckRequest;

import com.shopping.order.dto.inventoryDto.ReserveRequestDto;
import com.shopping.order.enums.InventoryStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="inventory-service")
public interface InventoryClient {

    @PostMapping("/api/v1/inventories/check")
    InventoryStatus checkProductAvailability(@RequestBody InventoryCheckRequest request);

    @PostMapping("/api/v1/inventories/reserve")
    ReserveRequestDto reserve(@RequestBody InventoryActionRequest request);
    @PostMapping("/api/v1/inventories/confirm")
    ReserveRequestDto confirm(@RequestBody InventoryActionRequest request);
    @PostMapping("/api/v1/inventories/release")
    ReserveRequestDto release(@RequestBody InventoryActionRequest request);

}
