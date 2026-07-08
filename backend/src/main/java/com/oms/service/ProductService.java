package com.oms.service;

import com.oms.dto.ProductDto;
import com.oms.entity.Category;
import com.oms.entity.Product;
import com.oms.entity.Stock;
import com.oms.repository.ProductRepository;
import com.oms.repository.StockRepository;
import com.oms.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StockRepository stockRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    public List<Product> searchProducts(String query, Integer categoryId) {
        if (categoryId != null && query != null && !query.trim().isEmpty()) {
            return productRepository.findByCategoryIdAndNameContainingIgnoreCaseOrCategoryIdAndSkuContainingIgnoreCase(categoryId, query, categoryId, query);
        } else if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId);
        } else if (query != null && !query.trim().isEmpty()) {
            return productRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(query, query);
        } else {
            return productRepository.findAll();
        }
    }

    @Transactional
    public Product createProduct(ProductDto productDto) {
        Category category = categoryService.getCategoryById(productDto.getCategoryId());

        Product product = Product.builder()
                .category(category)
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .gstPercent(productDto.getGstPercent())
                .imageUrl(productDto.getImageUrl())
                .sku(productDto.getSku())
                .build();

        Product savedProduct = productRepository.save(product);

        Stock stock = Stock.builder()
                .product(savedProduct)
                .quantity(0)
                .minimumRequired(5)
                .build();
        stockRepository.save(stock);

        return savedProduct;
    }

    @Transactional
    public Product updateProduct(Integer id, ProductDto productDto) {
        Product product = getProductById(id);
        Category category = categoryService.getCategoryById(productDto.getCategoryId());

        product.setCategory(category);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setGstPercent(productDto.getGstPercent());
        product.setImageUrl(productDto.getImageUrl());
        product.setSku(productDto.getSku());

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Integer id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
