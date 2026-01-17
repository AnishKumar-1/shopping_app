package com.shopping.order.repository;

import com.shopping.order.models.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItems,Long> {
}
