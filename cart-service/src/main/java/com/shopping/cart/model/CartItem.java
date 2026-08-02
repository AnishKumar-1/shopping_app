package com.shopping.cart.model;
import com.shopping.cart.dto.CartItemsDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="cart_items")
public class CartItem  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id",unique = true)
    private Long productId;
    private String productName;
    @Column(name="img_url",columnDefinition = "TEXT")
    private String imageUrl;
    private BigDecimal price;
    private Integer quantity;
}
