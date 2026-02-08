package com.shopping.payment.feignClient;

import com.shopping.payment.dto.OrderPaymentDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="payment-service", url = "${order-service.base-url}")
public interface PaymentClient {

    @GetMapping("/{order_id}")
    OrderPaymentDetailsDto SingleOrderDetails(@PathVariable Long order_id);
}
