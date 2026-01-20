package com.shopping.inventory.dtos;

import lombok.Data;

@Data
public class InventoryCheckRequest {
    private Long productId;
    private int quantity;
}
