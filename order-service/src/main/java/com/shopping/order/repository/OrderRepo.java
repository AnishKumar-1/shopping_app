package com.shopping.order.repository;

import com.shopping.order.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order,Long> {
    Page<Order> findAll(Pageable pageable);

}
