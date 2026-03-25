package com.miguel.ecommerce.product.service;

import com.miguel.ecommerce.product.dto.ProductRequest;
import com.miguel.ecommerce.product.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse save(ProductRequest productRequest);
    List<ProductResponse> findAllActive();
    List<ProductResponse> findAllByCategoryId(Long categoryId);
    ProductResponse findById(Long id);
    ProductResponse findBySku(String sku);
    ProductResponse update(Long id, ProductRequest productRequest);
    void deactivate(Long id);

}
