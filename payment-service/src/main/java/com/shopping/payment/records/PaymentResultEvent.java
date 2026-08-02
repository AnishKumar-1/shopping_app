package com.shopping.payment.records;

import java.util.UUID;

public record PaymentResultEvent(
        UUID eventId,
        Long paymentId,
        Long orderId,
        String status
) {}
