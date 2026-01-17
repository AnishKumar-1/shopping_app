package com.shopping.order.dto.orderItemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemsResponseDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;

}
