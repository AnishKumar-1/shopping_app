package com.shopping.order.utility;

import com.shopping.order.enums.OrderStatus;
import com.shopping.order.models.Order;
import com.shopping.order.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class Helper {

    private OrderRepo orderRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markOrderFailed(Long order_id){
        Order order = orderRepo.findById(order_id).orElseThrow();
        order.setStatus(OrderStatus.FAILED);
        orderRepo.save(order);
    }
}
