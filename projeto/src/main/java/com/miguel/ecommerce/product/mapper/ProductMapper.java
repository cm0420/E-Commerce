package com.miguel.ecommerce.product.mapper;

import com.miguel.ecommerce.product.dto.ProductRequest;
import com.miguel.ecommerce.product.dto.ProductResponse;
import com.miguel.ecommerce.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductResponse toProductResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequest productRequest);

    List<ProductResponse> toProductResponseList(List<Product> products);
}
