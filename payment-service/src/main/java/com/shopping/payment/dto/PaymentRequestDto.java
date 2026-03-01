package com.shopping.payment.dto;


import com.shopping.payment.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequestDto {
    @NotNull(message = "order id does not found.")
    private Long orderId;
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
