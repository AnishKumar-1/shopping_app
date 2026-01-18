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
import com.shopping.order.mappers.OrderItemsMapper;
import com.shopping.order.models.Order;
import com.shopping.order.models.OrderItems;
import com.shopping.order.repository.OrderItemRepo;
import com.shopping.order.repository.OrderRepo;
import jakarta.transaction.Transactional;
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
    @Autowired
    private OrderItemsMapper orderItemsMapper;


    //create order by taking order id and list of product items details
    @Transactional
    public OrderCreationResponseDto createOrder(OrderRequestDto orderRequestDto){

        Order order = Order.builder()
                .userId(orderRequestDto.getUserId())
                .status(OrderStatus.CREATED)
                .build();

        List<OrderItems> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (ProductItemsRequestDto productItems : orderRequestDto.getItems()) {

            ProductFeignResponseDto productDetails =
                    productClient.product(productItems.getProductId());

            BigDecimal subTotal = productDetails.getPrice()
                    .multiply(BigDecimal.valueOf(productItems.getQuantity()));

            totalAmount = totalAmount.add(subTotal);

            OrderItems items = OrderItems.builder()
                    .order(order)
                    .productId(productItems.getProductId())
                    .productName(productDetails.getName())
                    .price(productDetails.getPrice())
                    .quantity(productItems.getQuantity())
                    .subtotal(Double.valueOf(String.valueOf(subTotal)))
                    .build();

            orderItems.add(items);
        }
        order.setOrderItems(orderItems);
        order.setTotalAmount(Double.valueOf(String.valueOf(totalAmount))); // ✅ THIS WAS MISSING

        Order response = orderRepo.save(order);

        return OrderCreationResponseDto.builder()
                .orderId(response.getId())
                .status(response.getStatus().name())
                .build();
    }


  /*
  *  fetch all order details by passing order id
  *
  * */

    @Transactional
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
            OrderItemsResponseDto items=orderItemsMapper.mapToOrderItemsDto(orderItems);
            orderItemsResponseDto.add(items);

        }
       return  OrderResponseDto.builder()
                .orderId(response.getId())
                .status(String.valueOf(response.getStatus()))
                .totalAmount(totalAmount)
                .items(orderItemsResponseDto)
                .build();

    }

    // fetch all order details (admin)
    @Transactional
    public List<OrderResponseDto> fetch_all_orders() {

        List<Order> orders = orderRepo.findAllWithItems(); // fetch join

        return orders.stream()
                .map(order -> OrderResponseDto.builder()
                        .orderId(order.getId())
                        .status(order.getStatus().name())
                        .totalAmount(BigDecimal.valueOf(order.getTotalAmount())) // ✅ no conversion
                        .items(order.getOrderItems().stream()
                                .map(orderItemsMapper::mapToOrderItemsDto)
                                .toList())
                        .build())
                .toList();
    }



}
