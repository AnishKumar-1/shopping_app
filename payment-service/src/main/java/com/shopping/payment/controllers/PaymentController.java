package com.shopping.payment.controllers;
import com.shopping.payment.dto.PaymentOrderResponseDto;
import com.shopping.payment.dto.PaymentRequestDto;
import com.shopping.payment.dto.PaymentResponseDto;
import com.shopping.payment.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private PaymentService paymentService;

    //create payment
    @PostMapping
    public ResponseEntity<PaymentResponseDto> create_payment(@Valid @RequestBody PaymentRequestDto paymentRequestDto){
        return ResponseEntity.ok(paymentService.make_payment(paymentRequestDto));
    }

    //get payment details of a order by its id
    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentOrderResponseDto> paymentDetails(@PathVariable Long orderId){
        return ResponseEntity.ok(paymentService.payment_by_order_id(orderId));
    }


}
