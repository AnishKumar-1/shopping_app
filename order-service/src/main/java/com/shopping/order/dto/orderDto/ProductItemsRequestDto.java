package com.shopping.order.dto.orderDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemsRequestDto {
    @NotNull(message = "product id must not be empty")
    private Long productId;
    @NotNull(message = "product quantity must not be empty")
    @Min(value = 1,message = "product quantity must not be less than 1")
    private Integer quantity;
}
