package com.shopping.payment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderPaymentDetailsDto {

    private Long orderId;
    private BigDecimal totalAmount;
    private String status;

}
