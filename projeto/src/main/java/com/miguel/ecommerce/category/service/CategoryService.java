package com.miguel.ecommerce.category.service;

import com.miguel.ecommerce.category.dto.CategoryRequest;
import com.miguel.ecommerce.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse save(CategoryRequest categoryRequest);
    List<CategoryResponse> findAll();
    CategoryResponse findByID(Long id);
    void deactivate(Long id);
    CategoryResponse update(Long id, CategoryRequest categoryRequest);

}
