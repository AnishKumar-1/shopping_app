package com.shopping.payment.integration;

import com.shopping.payment.dto.OrderPaymentDetailsDto;
import com.shopping.payment.dto.PaymentRequestDto;
import com.shopping.payment.dto.UpdateOrderStatusRequest;
import com.shopping.payment.enums.OrderStatus;
import com.shopping.payment.exception.ServiceUnavailableException;
import com.shopping.payment.feignClient.OrderClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderIntegrationService {

    private final OrderClient orderClient;

    @CircuitBreaker(name="orderService", fallbackMethod = "orderFallBack")
    public OrderPaymentDetailsDto orderFeignResponse(PaymentRequestDto paymentRequestDto){
        return  orderClient.SingleOrderDetails(paymentRequestDto.getOrderId());
    }

    public OrderPaymentDetailsDto orderFallBack(PaymentRequestDto paymentRequestDto, Throwable ex){
        log.error("Order service failed: ",ex);
        throw new ServiceUnavailableException("Order service unavailable. Please try again later");
    }

    //update order status
    public void updateOrderStatus(UpdateOrderStatusRequest request,Long orderId){
        orderClient.updateOrderStatus(request,orderId);
    }
}
