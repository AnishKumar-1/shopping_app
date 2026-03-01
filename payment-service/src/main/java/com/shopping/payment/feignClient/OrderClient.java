package com.shopping.payment.feignClient;
import com.shopping.payment.dto.OrderPaymentDetailsDto;
import com.shopping.payment.dto.UpdateOrderStatusRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="order-service")
public interface OrderClient {

    @GetMapping("/api/v1/orders/{order_id}")
    OrderPaymentDetailsDto SingleOrderDetails(@PathVariable Long order_id);

    @PutMapping("/api/v1/orders/status/{orderId}")
    String updateOrderStatus(
            @RequestBody UpdateOrderStatusRequest request,
            @PathVariable Long orderId
    );
}
