package com.shopping.payment.services;

import com.shopping.payment.Events.CartEventProducer;
import com.shopping.payment.Events.PaymentEventProducer;
import com.shopping.payment.dto.*;
import com.shopping.payment.enums.OrderStatus;
import com.shopping.payment.enums.PaymentStatus;
import com.shopping.payment.exception.ResourceNotFoundException;
import com.shopping.payment.integration.OrderIntegrationService;
import com.shopping.payment.modules.Payment;
import com.shopping.payment.records.PaymentResultEvent;
import com.shopping.payment.repository.PaymentRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepo paymentRepo;
    private final OrderIntegrationService orderIntegrationService;
    private final CartEventProducer cartEventProducer;
    private final PaymentEventProducer paymentEventProducer;

    // create payment after order creation
    @Transactional
    public PaymentResponseDto make_payment(PaymentRequestDto paymentRequestDto){


        // 1️⃣ Fetch order
        OrderPaymentDetailsDto orderResponse =
                orderIntegrationService.orderFeignResponse(paymentRequestDto);


        Optional<Payment> existing =
                paymentRepo.findPaymentByOrderId(orderResponse.getOrderId());

        if(existing.isPresent()){
            log.error("❌ Duplicate payment detected for orderId: {}",
                    orderResponse.getOrderId());
            throw new IllegalStateException("Payment already done for this order");
        }

        // 3️⃣ Validate order status
        if(orderResponse.getStatus() != OrderStatus.PENDING) {
            log.error("❌ Invalid order status for payment: {}",
                    orderResponse.getStatus());
            throw new IllegalStateException(
                    "Payment not allowed for order in state: "
                            + orderResponse.getStatus()
            );
        }


        orderIntegrationService.updateOrderStatus(
                new UpdateOrderStatusRequest(OrderStatus.PAYMENT_PROCESSING),
                orderResponse.getOrderId()
        );

        // 5️⃣ Simulate payment
        boolean paymentSuccess = true;

        PaymentStatus paymentStatus =
                paymentSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

        // 6️⃣ Build payment object
        Payment payment = Payment.builder()
                .orderId(orderResponse.getOrderId())
                .paymentMethod(paymentRequestDto.getPaymentMethod())
                .status(paymentStatus)
                .amount(orderResponse.getTotalAmount())
                .build();


        Payment response = paymentRepo.save(payment);


        // 7️⃣ Update final order status
        if(paymentSuccess){
            log.info("✅ Updating order status to CONFIRMED for orderId: {}",
                    orderResponse.getOrderId());
            //replaced with kafka for the updation of order if service is down then later should be update
            PaymentResultEvent peymentEvent=new PaymentResultEvent(
                    UUID.randomUUID(),
                    response.getPaymentId(),
                    response.getOrderId(),
                    OrderStatus.CONFIRMED.name()
            );
            paymentEventProducer.sendPaymentResult(peymentEvent);
            CartClearEvent event = new CartClearEvent(UUID.randomUUID(),orderResponse.getOrderId());
            cartEventProducer.sendCartClearEvent(event);

        } else {

            //on failed should also update the order service via kafka event
            PaymentResultEvent peymentEvent=new PaymentResultEvent(
                    UUID.randomUUID(),
                    response.getPaymentId(),
                    response.getOrderId(),
                    OrderStatus.FAILED.name()
            );
        }

        return PaymentResponseDto.builder()
                .paymentId(response.getPaymentId())
                .status(paymentStatus)
                .build();
    }

    // payment by order id
    public PaymentOrderResponseDto payment_by_order_id(Long order_id){
        Payment payment = paymentRepo.findPaymentByOrderId(order_id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found for this orderId: " + order_id));

        return PaymentOrderResponseDto.builder()
                .orderId(payment.getOrderId())
                .paymentId(payment.getPaymentId())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .amount(payment.getAmount())
                .build();
    }
}