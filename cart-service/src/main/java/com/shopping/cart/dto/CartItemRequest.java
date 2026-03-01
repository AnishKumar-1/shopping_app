package com.shopping.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequest {
    @NotNull(message = "Product does not found")
    @Min(value = 1,message = "Product id cannot be 0")
    private Long productId;
    @NotNull(message = "Product quantity does not found")
    @Min(value=1,message = "Product quantity cannot be 0")
    private Integer quantity;
}
