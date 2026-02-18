package com.shopping.order.dto.inventoryDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryActionRequest {
    private Long productId;
    private Integer quantity;
}
