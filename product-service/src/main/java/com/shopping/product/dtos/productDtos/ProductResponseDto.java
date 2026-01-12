package com.shopping.product.dtos.productDtos;

import com.shopping.product.dtos.categoryDtos.CategorySummary;
import com.shopping.product.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;
}
