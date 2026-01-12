package com.shopping.inventory.repositories;


import com.shopping.inventory.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepo extends JpaRepository<Inventory,Long> {
    boolean existsByProductId(Long productId);
    Optional<Inventory> findByProductId(Long productId);
}
