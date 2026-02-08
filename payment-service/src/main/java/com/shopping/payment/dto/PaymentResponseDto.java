package com.shopping.payment.dto;

import com.shopping.payment.enums.PaymentMethod;
import com.shopping.payment.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto{

    private Long paymentId;
    private PaymentStatus status;

}
