package com.miguel.ecommerce.category.mapper;

import com.miguel.ecommerce.category.dto.CategoryRequest;
import com.miguel.ecommerce.category.dto.CategoryResponse;
import com.miguel.ecommerce.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    Category toEntity(CategoryRequest categoryRequest);

    List<CategoryResponse> toCategoryResponseList(List<Category> categories);
}
