package com.shopping.payment.dto;


import com.shopping.payment.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentRequestDto {
    @NotNull(message = "order id does not found.")
    private Long orderId;
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0", inclusive = true, message = "amount must be at least 1")
    private BigDecimal amount;
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
