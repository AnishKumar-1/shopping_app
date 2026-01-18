package com.shopping.order.repository;

import com.shopping.order.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Long> {
    @Query("select distinct o from Order o join fetch o.orderItems")
    List<Order> findAllWithItems();
}
