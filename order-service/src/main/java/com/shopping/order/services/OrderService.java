package com.shopping.order.services;

import com.shopping.order.FeignClient.ProductClient;
import com.shopping.order.dto.feignDto.ProductFeignResponseDto;
import com.shopping.order.dto.orderDto.OrderCreationResponseDto;
import com.shopping.order.dto.orderDto.OrderRequestDto;
import com.shopping.order.dto.orderDto.OrderResponseDto;
import com.shopping.order.dto.orderDto.ProductItemsRequestDto;
import com.shopping.order.dto.orderItemDto.OrderItemsResponseDto;
import com.shopping.order.enums.OrderStatus;
import com.shopping.order.exception.ResourceNotFound;
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
                     .price(productDetails.getPrice())
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

  /*
  *  fetch all order details by passing order id
  *
  * */

    public OrderResponseDto fetch_single_order(Long order_id){
        if(order_id==null || order_id <= 0){
            throw new IllegalArgumentException("OrderId must not be empty and must be greater than 0 " + order_id);
        }
        Order response=orderRepo.findById(order_id).orElseThrow(()->new ResourceNotFound("Order not found with this id " + order_id));

        List<OrderItemsResponseDto> orderItemsResponseDto= new ArrayList<>();
        BigDecimal totalAmount=BigDecimal.ZERO;
        for(OrderItems orderItems: response.getOrderItems()){

            BigDecimal itemTotal = orderItems.getPrice()
                    .multiply(BigDecimal.valueOf(orderItems.getQuantity()));

            totalAmount = totalAmount.add(itemTotal);

            OrderItemsResponseDto items=OrderItemsResponseDto.builder()
                    .productId(orderItems.getProductId())
                    .productName(orderItems.getProductName())
                    .quantity(orderItems.getQuantity())
                    .price(orderItems.getPrice())
                    .build();
            orderItemsResponseDto.add(items);

        }
       return  OrderResponseDto.builder()
                .orderId(response.getId())
                .status(String.valueOf(response.getStatus()))
                .totalAmount(totalAmount)
                .items(orderItemsResponseDto)
                .build();

    }


}
