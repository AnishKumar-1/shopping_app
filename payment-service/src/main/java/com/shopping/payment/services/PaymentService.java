package com.shopping.payment.services;
import com.shopping.payment.dto.*;
import com.shopping.payment.enums.OrderStatus;
import com.shopping.payment.enums.PaymentStatus;
import com.shopping.payment.exception.InvalidOrderStateException;
import com.shopping.payment.exception.ResourceNotFoundException;
import com.shopping.payment.feignClient.CartClient;
import com.shopping.payment.feignClient.OrderClient;
import com.shopping.payment.modules.Payment;
import com.shopping.payment.repository.PaymentRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderClient orderClient;
    private final PaymentRepo paymentRepo;
    private final CartClient cartClient;


    //create payment after order creation
    @Transactional
    public PaymentResponseDto make_payment(PaymentRequestDto paymentRequestDto){
        // 1️⃣ Fetch order
        OrderPaymentDetailsDto orderResponse =
                orderClient.SingleOrderDetails(paymentRequestDto.getOrderId());

        // ✅ ADD THIS VALIDATION HERE
        if(orderResponse.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Payment not allowed for order in state: "
                            + orderResponse.getStatus()
            );
        }

        // 3️⃣ Mark order as PAYMENT_PROCESSING
        orderClient.updateOrderStatus(
                new UpdateOrderStatusRequest(OrderStatus.PAYMENT_PROCESSING),
                orderResponse.getOrderId()
        );

        // 4️⃣ Simulate payment
        boolean paymentSuccess = true; // dummy for now

        PaymentStatus paymentStatus =
                paymentSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

        // 5️⃣ Save payment
        Payment payment = Payment.builder()
                .orderId(orderResponse.getOrderId())
                .paymentMethod(paymentRequestDto.getPaymentMethod())
                .status(paymentStatus)
                .amount(orderResponse.getTotalAmount())
                .build();

        Payment response = paymentRepo.save(payment);

        //  Update order final status
        if(paymentSuccess){
            orderClient.updateOrderStatus(
                    new UpdateOrderStatusRequest(OrderStatus.CONFIRMED),
                    orderResponse.getOrderId()
            );
            cartClient.clear_cart();
        } else {
            orderClient.updateOrderStatus(
                    new UpdateOrderStatusRequest(OrderStatus.FAILED),
                    orderResponse.getOrderId()
            );
        }

        return PaymentResponseDto.builder()
                .paymentId(response.getPaymentId())
                .status(paymentStatus)
                .build();
    }



    //payment by order id
    public PaymentOrderResponseDto payment_by_order_id(Long order_id){
       Payment payment=paymentRepo.findPaymentByOrderId(order_id).orElseThrow(()->
               new ResourceNotFoundException("Payment not found for this orderId: " + order_id));
       return PaymentOrderResponseDto.builder().orderId(payment.getOrderId()).paymentId(payment.getPaymentId())
               .paymentMethod(payment.getPaymentMethod()).status(payment.getStatus()).amount(payment.getAmount()).build();
    }
}
