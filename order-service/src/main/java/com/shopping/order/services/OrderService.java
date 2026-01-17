package com.shopping.order.services;

import com.shopping.order.FeignClient.ProductClient;
import com.shopping.order.dto.feignDto.ProductFeignResponseDto;
import com.shopping.order.dto.orderDto.OrderCreationResponseDto;
import com.shopping.order.dto.orderDto.OrderRequestDto;
import com.shopping.order.dto.orderDto.ProductItemsRequestDto;
import com.shopping.order.enums.OrderStatus;
import com.shopping.order.models.Order;
import com.shopping.order.models.OrderItems;
import com.shopping.order.repository.OrderItemRepo;
import com.shopping.order.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private ProductClient productClient;


    //create order by taking order id and list of product items details
    public OrderCreationResponseDto createOrder(OrderRequestDto orderRequestDto){

         Order order= Order.builder().userId(orderRequestDto.getUserId())
                 .status(OrderStatus.CREATED)
                 .build();

         List<OrderItems> orderItems=new ArrayList<>();

         for(ProductItemsRequestDto productItems:orderRequestDto.getItems()){
             ProductFeignResponseDto productDetails= productClient.product(productItems.getProductId());
             OrderItems items=OrderItems.builder()
                     .productId(productItems.getProductId())
                     .productName(productDetails.getName())
                     .price(productDetails.getPrice().doubleValue())
                     .quantity(productItems.getQuantity())
                     .subtotal(
                             productDetails.getPrice().multiply(BigDecimal.valueOf(productItems.getQuantity())).doubleValue()
                     ).order(order).build();
             orderItems.add(items);
         }
         order.setOrderItems(orderItems);
        Order response=orderRepo.save(order);

        return OrderCreationResponseDto.builder()
                .orderId(response.getId())
                .status(String.valueOf(response.getStatus())).build();
    }



}
