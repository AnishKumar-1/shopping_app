package com.shopping.order.dto.orderDto;

import com.shopping.order.dto.orderItemDto.OrderItemsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private Long orderId;
    private String status;
    private BigDecimal totalAmount;
    private List<OrderItemsResponseDto> items;

}
