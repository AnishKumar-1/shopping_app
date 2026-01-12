package com.shopping.product.repositories;

import com.shopping.product.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {
    List<Product> findByCategoryId(Long categoryId);
}
