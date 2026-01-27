package com.shopping.inventory.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryCheckRequest {
    @NotNull(message = "product id not found")
    private Long productId;
    @NotNull(message = "product quantity not found")
    @Min(value = 1,message = "product quantity cannot be 0")
    private Integer quantity;
}
