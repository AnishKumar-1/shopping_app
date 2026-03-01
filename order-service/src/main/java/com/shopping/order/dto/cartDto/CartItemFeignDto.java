package com.shopping.order.dto.cartDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemFeignDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal priceAtTime;
    private String imageUrl;
}
