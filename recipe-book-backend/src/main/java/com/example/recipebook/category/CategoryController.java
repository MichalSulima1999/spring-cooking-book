package com.example.recipebook.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public record CategoryController(CategoryService categoryService) {
    @Operation(summary = "Get all categories")
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok().body(categoryService.getAllCategories());
    }

    @Operation(summary = "Get a few categories by name")
    @GetMapping
    public ResponseEntity<List<Category>> getLimitedCategoriesByDescription(@Parameter(description = "Category name like")
                                                                            @RequestParam(name = "name") String categoryName) {
        return ResponseEntity.ok().body(categoryService.getLimitedCategoriesByName(categoryName));
    }

    @Operation(summary = "Add category")
    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/category").toUriString());
        return ResponseEntity.created(uri).body(categoryService.addCategory(category));
    }
}
