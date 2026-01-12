package com.shopping.inventory.dtos;

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
public class InventoryRequestDto {
    @NotNull(message = "Please provide total quantity")
    @Min(value = 1,message = "total quantity must be greater than 0 ")
    private Integer totalQuantity;

}
