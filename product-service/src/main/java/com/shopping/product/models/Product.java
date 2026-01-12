package com.shopping.product.models;

import com.shopping.product.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="product_name",nullable = false)
    private String name;
    @Column(name="product_description")
    private String description;
    @Column(name="product_status")
    @Enumerated(EnumType.STRING)
    private Status status=Status.ACTIVE;
    @Column(name="product_price")
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name="updated_at")
    private Instant updatedAt;

}
