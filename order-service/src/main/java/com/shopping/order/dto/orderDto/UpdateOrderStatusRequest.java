package com.shopping.order.dto.orderDto;

import com.shopping.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {

    @NotNull(message = "Order status must not be null")
    private OrderStatus orderStatus;
}
