package com.miguel.ecommerce.stock.service;

import com.miguel.ecommerce.product.dto.ProductResponse;
import com.miguel.ecommerce.stock.dto.StockRequest;
import com.miguel.ecommerce.stock.dto.StockResponse;

public interface StockService {
    StockResponse removeQuantity(Long productId, StockRequest stockRequest);
    StockResponse addQuantity(Long productId, StockRequest stockRequest);
    StockResponse findByProductId(Long productId);

}
