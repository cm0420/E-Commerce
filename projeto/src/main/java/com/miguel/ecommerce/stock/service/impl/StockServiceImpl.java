package com.miguel.ecommerce.stock.service.impl;

import com.miguel.ecommerce.excpetion.BusinessException;
import com.miguel.ecommerce.excpetion.ResourceNotFoundException;
import com.miguel.ecommerce.product.repository.ProductRepository;
import com.miguel.ecommerce.stock.dto.StockRequest;
import com.miguel.ecommerce.stock.dto.StockResponse;
import com.miguel.ecommerce.stock.entity.Stock;
import com.miguel.ecommerce.stock.mapper.StockMapper;
import com.miguel.ecommerce.stock.repository.StockRepository;
import com.miguel.ecommerce.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    @Override
    public StockResponse removeQuantity(Long productId, StockRequest stockRequest) {
        Stock stock = stockRepository.findByProductId(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Stock", productId));
        if (stock.getQuantity() < stockRequest.quantity()) {
            throw new BusinessException("Insufficient Stock");
        }
        stock.setQuantity(stock.getQuantity() - stockRequest.quantity());
        return stockMapper.toStockResponse(stockRepository.save(stock));

    }

    @Override
    public StockResponse addQuantity(Long productId, StockRequest stockRequest) {
        Stock stock = stockRepository.findByProductId(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Stock", productId));
        stock.setQuantity(stock.getQuantity() + stockRequest.quantity());
        return stockMapper.toStockResponse(stockRepository.save(stock));

    }

    @Override
    public StockResponse findByProductId(Long productId) {
        Stock stock = stockRepository.findByProductId(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Stock", productId));
        return stockMapper.toStockResponse(stock);
    }
}
