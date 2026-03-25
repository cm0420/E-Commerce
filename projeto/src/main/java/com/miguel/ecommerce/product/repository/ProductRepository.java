package com.miguel.ecommerce.product.repository;

import com.miguel.ecommerce.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySku(String sku);
    List<Product> findAllByIsActiveTrue();
    List<Product> findAllByCategoryIdAndIsActiveTrue(Long categoryId);
    Optional<Product> findBysku(String sku);


}
