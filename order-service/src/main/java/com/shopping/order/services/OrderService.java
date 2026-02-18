package com.shopping.order.services;

import com.shopping.order.FeignClient.InventoryClient;
import com.shopping.order.FeignClient.ProductClient;
import com.shopping.order.dto.feignDto.ProductFeignResponseDto;
import com.shopping.order.dto.inventoryDto.InventoryActionRequest;
import com.shopping.order.dto.inventoryDto.InventoryCheckRequest;
import com.shopping.order.dto.inventoryDto.InventoryCheckResponse;
import com.shopping.order.dto.orderDto.*;
import com.shopping.order.dto.orderItemDto.OrderItemsResponseDto;
import com.shopping.order.enums.InventoryStatus;
import com.shopping.order.enums.OrderStatus;
import com.shopping.order.exception.InventoryReservationException;
import com.shopping.order.exception.OutOfStockException;
import com.shopping.order.exception.ResourceNotFound;
import com.shopping.order.mappers.OrderItemsMapper;
import com.shopping.order.models.Order;
import com.shopping.order.models.OrderItems;
import com.shopping.order.repository.OrderItemRepo;
import com.shopping.order.repository.OrderRepo;
import com.shopping.order.utility.Helper;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductClient productClient;
    private final OrderItemsMapper orderItemsMapper;
    private final InventoryClient inventoryClient;
    private final Helper helper;

    //create order by taking order id and list of product items details
    @Transactional
    public OrderCreationResponseDto createOrder(OrderRequestDto orderRequestDto){

//        inventoryClient.checkProductAvailability()

        for(ProductItemsRequestDto productItemsRequestDto: orderRequestDto.getItems()){
            InventoryStatus status = inventoryClient.checkProductAvailability(
                    new InventoryCheckRequest(productItemsRequestDto.getProductId(),productItemsRequestDto.getQuantity())
            );
            if(status  == InventoryStatus.OUT_OF_STOCK){
                throw new OutOfStockException("Cannot create order some of products are out of stock");
            }
        }
        Order order = Order.builder()
                .userId(orderRequestDto.getUserId())
                .status(OrderStatus.PENDING)
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
                    .subtotal(subTotal)
                    .build();

            orderItems.add(items);
        }
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount); //

        Order response = orderRepo.save(order);

        // after order is created reserved the quantity of product
        //will get product id and quantity in a list and single user id
       //tracking reserved inventory if order failed then can release it
        List<OrderItems> reservedItems=new ArrayList<>();
        try{
             //Reserve inventory after order creation for the product
             for(OrderItems orderItems1: response.getOrderItems()){
                 inventoryClient.reserve(
                         new InventoryActionRequest(
                                 orderItems1.getProductId(),
                                 orderItems1.getQuantity())
                 );

                 reservedItems.add(orderItems1);
             }


        }catch (FeignException e){

              //compansation logic
            for(OrderItems releaseItem: reservedItems){
                inventoryClient.release(
                        new InventoryActionRequest(
                                releaseItem.getProductId(),releaseItem.getQuantity())
                );
            }
             helper.markOrderFailed(response.getId());
            throw new InventoryReservationException("Failed to reserve inventory for order " + response.getId()
            );
        }

        return OrderCreationResponseDto.builder()
                .orderId(response.getId())
                .status(response.getStatus().name())
                .build();
    }




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
    /*
     *  fetch all order details by passing order id
     *
     * */
    @Transactional
    public List<OrderResponseDto> fetch_all_orders(Pageable pageable) {

        Page<Order> orders = orderRepo.findAll(pageable); // fetch join

        return orders.stream()
                .map(order -> OrderResponseDto.builder()
                        .orderId(order.getId())
                        .status(order.getStatus().name())
                        .totalAmount(order.getTotalAmount()) // ✅ no conversion
                        .items(order.getOrderItems().stream()
                                .map(orderItemsMapper::mapToOrderItemsDto)
                                .toList())
                        .build())
                .toList();
    }


    @Transactional
    public String update_order_status(UpdateOrderStatusRequest statusReq, Long order_id){

        Order order = orderRepo.findById(order_id)
                .orElseThrow(() -> new ResourceNotFound("Order not found"));

        if(order.getStatus() != OrderStatus.PENDING){
            throw new IllegalStateException("Only PENDING orders can change status");
        }

        OrderStatus newStatus = statusReq.getOrderStatus();

        try {

            if(newStatus == OrderStatus.CONFIRMED) {
                order.getOrderItems().forEach(item ->
                        inventoryClient.confirm(
                                new InventoryActionRequest(
                                        item.getProductId(),
                                        item.getQuantity()
                                )
                        )
                );
            }

            if(newStatus == OrderStatus.FAILED) {
                order.getOrderItems().forEach(item ->
                        inventoryClient.release(
                                new InventoryActionRequest(
                                        item.getProductId(),
                                        item.getQuantity()
                                )
                        )
                );
            }

        } catch (FeignException e) {
            throw new RuntimeException("Inventory synchronization failed");
        }

        order.setStatus(newStatus);

        return "Order status updated successfully.";
    }


}
