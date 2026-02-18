package com.shopping.payment.feignClient;

import com.shopping.payment.dto.OrderPaymentDetailsDto;
import com.shopping.payment.dto.UpdateOrderStatusRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="payment-service", url = "${order-service.base-url}")
public interface OrderClient {

    @GetMapping("/{order_id}")
    OrderPaymentDetailsDto SingleOrderDetails(@PathVariable Long order_id);

    @PatchMapping("/api/v1/orders/status/{orderId}")
    String updateOrderStatus(
            @RequestBody UpdateOrderStatusRequest request,
            @PathVariable Long orderId
    );
}
