package com.shopping.order.services;

import com.shopping.order.FeignClient.InventoryClient;
import com.shopping.order.FeignClient.ProductClient;
import com.shopping.order.dto.feignDto.ProductFeignResponseDto;
import com.shopping.order.dto.inventoryDto.InventoryCheckRequest;
import com.shopping.order.dto.inventoryDto.InventoryCheckResponse;
import com.shopping.order.dto.orderDto.OrderCreationResponseDto;
import com.shopping.order.dto.orderDto.OrderRequestDto;
import com.shopping.order.dto.orderDto.OrderResponseDto;
import com.shopping.order.dto.orderDto.ProductItemsRequestDto;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private OrderRepo orderRepo;
    private OrderItemRepo orderItemRepo;
    private ProductClient productClient;
    private OrderItemsMapper orderItemsMapper;
    private InventoryClient inventoryClient;
    private Helper helper;

    //create order by taking order id and list of product items details
    @Transactional
    public OrderCreationResponseDto createOrder(OrderRequestDto orderRequestDto){

//        inventoryClient.checkProductAvailability()

        for(ProductItemsRequestDto productItemsRequestDto: orderRequestDto.getItems()){
            InventoryCheckResponse response= inventoryClient.checkProductAvailability(
                    new InventoryCheckRequest(productItemsRequestDto.getProductId(),productItemsRequestDto.getQuantity())
            );
            if(response.getStatus() == InventoryStatus.OUT_OF_STOCK){
                throw new OutOfStockException("Cannot create order some of products are out of stock");
            }
        }
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

        // after order is created reserved the quantity of product
        //will get product id and quantity in a list and single user id
        try{
            response.getOrderItems().stream().forEach(orderItem->
                    inventoryClient.reserve(orderItem.getProductId(),orderItem.getQuantity()));
        }catch (FeignException e){
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
