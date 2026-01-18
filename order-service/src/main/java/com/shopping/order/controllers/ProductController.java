package com.shopping.order.controllers;

import com.shopping.order.dto.orderDto.OrderCreationResponseDto;
import com.shopping.order.dto.orderDto.OrderRequestDto;
import com.shopping.order.dto.orderDto.OrderResponseDto;
import com.shopping.order.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class ProductController {

    @Autowired
    private OrderService orderService;

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
}
