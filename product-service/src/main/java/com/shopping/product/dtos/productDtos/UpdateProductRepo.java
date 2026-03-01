package com.shopping.product.dtos.productDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRepo {
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
}
