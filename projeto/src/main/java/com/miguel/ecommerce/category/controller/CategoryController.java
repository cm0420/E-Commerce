package com.miguel.ecommerce.category.controller;

import com.miguel.ecommerce.category.dto.CategoryRequest;
import com.miguel.ecommerce.category.dto.CategoryResponse;
import com.miguel.ecommerce.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponse> save(@Valid @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(categoryRequest));
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponse> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.findByID(id));
    }

    @GetMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryResponse>>findAll(){return ResponseEntity.ok(categoryService.findAll());}

    @PatchMapping(value = "/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable("id") Long id) {
        categoryService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponse> update(@PathVariable("id") Long id, @Valid @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.update(id, categoryRequest));
    }
}
