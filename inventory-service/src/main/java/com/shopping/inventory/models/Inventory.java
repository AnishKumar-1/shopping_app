package com.shopping.inventory.models;

import com.shopping.inventory.enums.InventoryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="product_id",nullable = false,unique = true)
    private Long productId;
    @Column(name="total_quantity",nullable = false)
    @Builder.Default
    private Integer totalQuantity=0;
    @Version
    private Long version;
    @Column(name="available_quantity")
    @Builder.Default
    private Integer availableQuantity=0;
    @Column(name = "reserved_quantity", nullable = false)
    @Builder.Default
    private Integer reservedQuantity=0;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private InventoryStatus status;
    @Column(name="created_at",updatable = false)
    private LocalDateTime createdAt;
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist(){
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

}
