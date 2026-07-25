package com.shopping.payment.dto;

import java.util.UUID;

public record CartClearEvent (
     UUID eventId,
     Long orderId
){}

