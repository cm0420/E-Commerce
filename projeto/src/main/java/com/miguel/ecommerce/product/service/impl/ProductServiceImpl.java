package com.miguel.ecommerce.product.service.impl;

import com.miguel.ecommerce.category.entity.Category;
import com.miguel.ecommerce.category.repository.CategoryRepository;
import com.miguel.ecommerce.excpetion.BusinessException;
import com.miguel.ecommerce.excpetion.ResourceNotFoundException;
import com.miguel.ecommerce.product.dto.ProductRequest;
import com.miguel.ecommerce.product.dto.ProductResponse;
import com.miguel.ecommerce.product.entity.Product;
import com.miguel.ecommerce.product.mapper.ProductMapper;
import com.miguel.ecommerce.product.repository.ProductRepository;
import com.miguel.ecommerce.product.service.ProductService;
import com.miguel.ecommerce.stock.entity.Stock;
import com.miguel.ecommerce.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final StockRepository stockRepository;

    @Override
    public ProductResponse save(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.categoryId()).
                orElseThrow(() -> new ResourceNotFoundException("Category", productRequest.categoryId()));
        if (productRepository.existsBySku(productRequest.sku())) {
            throw new BusinessException("Product already exists");
        }
        Product product = productMapper.toEntity(productRequest);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        Stock stock = Stock.builder()
                .product(savedProduct)
                .quantity(0)
                .build();

        stockRepository.save(stock);

        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public List<ProductResponse> findAllActive() {
        return productMapper.toProductResponseList(productRepository.findAllByIsActiveTrue());
    }

    @Override
    public List<ProductResponse> findAllByCategoryId(Long categoryId) {
        return productMapper.toProductResponseList(productRepository.findAllByCategoryIdAndIsActiveTrue(categoryId));
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return productMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse findBySku(String sku) {
        Product product = productRepository.findBysku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product", 0L));
        return productMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", id));
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setSku(productRequest.sku());
        product.setImageUrl(productRequest.imageUrl());
        return productMapper.toProductResponse(productRepository.save(product));

    }

    @Override
    public void deactivate(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", id));
        product.setIsActive(false);
        productRepository.save(product);
    }
}
