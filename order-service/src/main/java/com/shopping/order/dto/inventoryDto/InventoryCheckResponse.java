package com.shopping.order.dto.inventoryDto;

import com.shopping.order.enums.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCheckResponse {
   private InventoryStatus status;
}
