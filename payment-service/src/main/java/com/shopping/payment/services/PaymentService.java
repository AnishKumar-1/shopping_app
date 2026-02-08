package com.shopping.payment.services;
import com.shopping.payment.dto.OrderPaymentDetailsDto;
import com.shopping.payment.dto.PaymentOrderResponseDto;
import com.shopping.payment.dto.PaymentRequestDto;
import com.shopping.payment.dto.PaymentResponseDto;
import com.shopping.payment.enums.PaymentStatus;
import com.shopping.payment.exception.InvalidOrderStateException;
import com.shopping.payment.exception.ResourceNotFoundException;
import com.shopping.payment.feignClient.PaymentClient;
import com.shopping.payment.modules.Payment;
import com.shopping.payment.repository.PaymentRepo;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private PaymentClient paymentClient;
    private PaymentRepo paymentRepo;

    //create payment after order creation
    public PaymentResponseDto make_payment(PaymentRequestDto paymentRequestDto){

        OrderPaymentDetailsDto orderResponse=paymentClient.SingleOrderDetails(paymentRequestDto.getOrderId());

        if(!orderResponse.getStatus().equals("CREATED")){
            throw new InvalidOrderStateException("Payment not allowed for this order: " + orderResponse.getStatus());
        }
        Payment payment=Payment.builder().orderId(orderResponse.getOrderId()).paymentMethod(paymentRequestDto.getPaymentMethod())
                .status(PaymentStatus.SUCCESS)
                .amount(paymentRequestDto.getAmount()).build();

        Payment response=paymentRepo.save(payment);

        return PaymentResponseDto.builder().paymentId(response.getPaymentId()).status(PaymentStatus.SUCCESS).build();

    }


    //payment by order id
    public PaymentOrderResponseDto payment_by_order_id(Long order_id){
       Payment payment=paymentRepo.findPaymentByOrderId(order_id).orElseThrow(()->
               new ResourceNotFoundException("Payment not found for this orderId: " + order_id));
       return PaymentOrderResponseDto.builder().orderId(payment.getOrderId()).paymentId(payment.getPaymentId())
               .paymentMethod(payment.getPaymentMethod()).status(payment.getStatus()).amount(payment.getAmount()).build();
    }
}
