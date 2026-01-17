package com.shopping.order.dto.orderDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    @NotNull(message = "user id does not found")
    private Long userId;
    @Valid
    private List<ProductItemsRequestDto> items;

}
