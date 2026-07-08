package com.oms.repository;

import com.oms.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategoryId(Integer categoryId);
    List<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String name, String sku);
    List<Product> findByCategoryIdAndNameContainingIgnoreCaseOrCategoryIdAndSkuContainingIgnoreCase(Integer categoryId, String name, Integer categoryId2, String sku);
    Optional<Product> findBySku(String sku);
}
