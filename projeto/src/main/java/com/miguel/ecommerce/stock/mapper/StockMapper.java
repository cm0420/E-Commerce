package com.miguel.ecommerce.stock.mapper;

import com.miguel.ecommerce.stock.dto.StockRequest;
import com.miguel.ecommerce.stock.dto.StockResponse;
import com.miguel.ecommerce.stock.entity.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMapper {
    @Mapping(target = "productId", source = "product.id")
    StockResponse toStockResponse(Stock stock);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "quantity", constant = "0")
    Stock toEntity(StockRequest stockRequest);

    List<StockResponse> toStockResponseList(List<Stock> stocks);
}
