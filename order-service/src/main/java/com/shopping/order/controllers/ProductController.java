package com.shopping.order.controllers;

import com.shopping.order.dto.orderDto.OrderCreationResponseDto;
import com.shopping.order.dto.orderDto.OrderRequestDto;
import com.shopping.order.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
