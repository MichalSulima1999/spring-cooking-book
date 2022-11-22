package com.example.recipebook.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public record CategoryController(CategoryService categoryService) {
    @Operation(summary = "Get a few categories by name")
    @GetMapping
    public ResponseEntity<List<Category>> getLimitedCategoriesByDescription(@Parameter(description = "Category name like")
                                                                            @RequestParam(name = "name") String categoryName) {
        return ResponseEntity.ok().body(categoryService.getLimitedCategoriesByName(categoryName));
    }
}
