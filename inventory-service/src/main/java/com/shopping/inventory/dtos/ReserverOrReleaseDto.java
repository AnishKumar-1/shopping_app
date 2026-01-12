package com.shopping.inventory.dtos;

import com.shopping.inventory.enums.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserverOrReleaseDto {
    private Long productId;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private InventoryStatus status;
}
