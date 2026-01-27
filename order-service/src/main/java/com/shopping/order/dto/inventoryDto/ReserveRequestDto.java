package com.shopping.order.dto.inventoryDto;


import lombok.Data;

@Data
public class ReserveRequestDto  {
        private Long productId;
        private Integer reservedQuantity;

}
