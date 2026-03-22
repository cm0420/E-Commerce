package com.miguel.ecommerce.category.service.impl;

import com.miguel.ecommerce.category.dto.CategoryRequest;
import com.miguel.ecommerce.category.dto.CategoryResponse;
import com.miguel.ecommerce.category.entity.Category;
import com.miguel.ecommerce.category.mapper.CategoryMapper;
import com.miguel.ecommerce.category.repository.CategoryRepository;
import com.miguel.ecommerce.category.service.CategoryService;
import com.miguel.ecommerce.excpetion.BusinessException;
import com.miguel.ecommerce.excpetion.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Override
    public CategoryResponse save(CategoryRequest categoryRequest) {
        if(categoryRepository.existsByName(categoryRequest.name())){
            throw new BusinessException("Category already exists");
        }
        Category category = categoryMapper.toEntity(categoryRequest);
        category.setIsActive(true);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryMapper.toCategoryResponseList(categoryRepository.findAllByIsActiveTrue());
    }

    @Override
    public CategoryResponse findByID(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Category not found", id));
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public void deactivate(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Category not found", id));
        category.setIsActive(false);
        categoryRepository.save(category);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Category not found", id));
        category.setName(categoryRequest.name());
        category.setDescription(categoryRequest.description());

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }
}
