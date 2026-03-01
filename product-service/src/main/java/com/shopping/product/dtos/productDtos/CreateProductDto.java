package com.shopping.product.dtos.productDtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDto {

    @NotEmpty(message = "product name not found")
    private String name;
    @NotEmpty(message = "product description not found")
    private String description;
    @NotNull(message = "product price not found")
    private BigDecimal price;
    @NotEmpty(message = "image url not found")
    private String imageUrl;
}
