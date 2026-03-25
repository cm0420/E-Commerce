package com.miguel.ecommerce.product.controller;

import com.miguel.ecommerce.product.dto.ProductRequest;
import com.miguel.ecommerce.product.dto.ProductResponse;
import com.miguel.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<ProductResponse> save(@Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productRequest));
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(productService.findAllActive());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> findByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping(value = "/sku/{sku}",
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> findBySku(@PathVariable("sku") String sku) {
        return ResponseEntity.ok(productService.findBySku(sku));
    }

    @GetMapping(value = "/categories/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> findAllByCategoryId(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(productService.findAllByCategoryId(categoryId));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.update(id, productRequest));
    }

    @PatchMapping(value = "/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable("id") Long id) {
        productService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

}
