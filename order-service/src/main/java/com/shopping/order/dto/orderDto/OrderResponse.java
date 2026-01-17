package com.shopping.order.dto.orderDto;

import com.shopping.order.dto.orderItemDto.OrderItemsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private String status;
    private Double totalAmount;
    private List<OrderItemsResponseDto> items;

}
