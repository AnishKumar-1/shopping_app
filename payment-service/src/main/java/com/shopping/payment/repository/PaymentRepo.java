package com.shopping.payment.repository;

import com.shopping.payment.modules.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
   public Optional<Payment> findPaymentByOrderId(Long orderId);
}
