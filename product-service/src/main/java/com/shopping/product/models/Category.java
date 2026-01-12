package com.shopping.product.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="product_category")
@EntityListeners(AuditingEntityListener.class)
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String description;
    @Builder.Default
    private Boolean active=true;
    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name="updated_at")
    private Instant updatedAt;
}
