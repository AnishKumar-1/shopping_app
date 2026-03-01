package com.shopping.payment.dto;

import com.shopping.payment.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderPaymentDetailsDto {

    private Long orderId;
    private BigDecimal totalAmount;
    private OrderStatus status;

}
