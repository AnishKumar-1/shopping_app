package com.shopping.order.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
    @Table(name="order_items")
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class OrderItems {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @JoinColumn(name = "order_id")
        @ManyToOne(fetch = FetchType.LAZY)
        private Order order;
        @Column(name="product_id")
        private Long productId;
        @Column(name="product_name")
        private String productName;
        @Column(name="product_price")
        private BigDecimal price;
        private Integer quantity;
        @Column(name="sub_total")
        private BigDecimal subtotal;

}
