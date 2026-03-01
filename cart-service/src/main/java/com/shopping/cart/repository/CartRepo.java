package com.shopping.cart.repository;

import com.shopping.cart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}
