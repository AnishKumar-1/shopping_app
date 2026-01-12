package com.shopping.product.repositories;

import com.shopping.product.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepo extends JpaRepository<Category,Long> {
    boolean existsByName(String name);
}
