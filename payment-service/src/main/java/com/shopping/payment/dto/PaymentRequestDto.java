package com.shopping.payment.dto;


import com.shopping.payment.enums.PaymentMethod;
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
    @NotNull(message = "amount does not found.")
    @Positive(message = "mount cannot be negative.")
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
}
