package com.miguel.ecommerce.stock.controller;

import com.miguel.ecommerce.stock.dto.StockRequest;
import com.miguel.ecommerce.stock.dto.StockResponse;
import com.miguel.ecommerce.stock.entity.Stock;
import com.miguel.ecommerce.stock.service.StockService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping(value = "/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockResponse> findByProductId(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(stockService.findByProductId(productId));
    }

    @PatchMapping(value = "/product/{productId}/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockResponse> addQuantity(
            @PathVariable Long productId,
            @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(stockService.addQuantity(productId, request));
    }

    @PatchMapping(value = "/product/{productId}/remove",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockResponse> removeQuantity(
            @PathVariable Long productId,
            @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(stockService.removeQuantity(productId, request));
    }

}
