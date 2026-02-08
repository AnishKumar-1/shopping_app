package com.shopping.payment.dto;

import com.shopping.payment.enums.PaymentMethod;
import com.shopping.payment.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentOrderResponseDto {
    private Long orderId;
    private Long paymentId;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private BigDecimal amount;
}
