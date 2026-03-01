package com.shopping.order.controllers;

import com.shopping.order.dto.orderDto.*;
import com.shopping.order.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //create order by taking user id and items details like product id and its quantity from body

    @PostMapping
    public ResponseEntity<OrderCreationResponseDto> createOrder(@Valid @RequestBody OrderRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    //get order details by its id
    @GetMapping("/{order_id}")
    public ResponseEntity<OrderResponseDto> SingleOrderDetails(@PathVariable Long order_id){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.fetch_single_order(order_id));
    }

    //fetch all order by pagination
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> fetch_all_orders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
      Pageable pageable= PageRequest.of(page,size);
        return ResponseEntity.status(HttpStatus.OK).body(orderService.fetch_all_orders(pageable));
    }

   //update order status
    @PutMapping("/status/{orderId}")
    public ResponseEntity<String> update_order_status(@Valid @RequestBody UpdateOrderStatusRequest statusReq,@PathVariable Long orderId){
     return ResponseEntity.status(HttpStatus.OK).body(orderService.update_order_status(statusReq,orderId));
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderCreationResponseDto> checkout(
            @RequestBody CheckoutRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.checkout(request));
    }
}
