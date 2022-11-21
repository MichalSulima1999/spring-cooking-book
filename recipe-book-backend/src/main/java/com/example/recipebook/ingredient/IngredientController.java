package com.example.recipebook.ingredient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ingredient")
public record IngredientController(IngredientService ingredientService) {
    @Operation(summary = "Get a few ingredients by name")
    @GetMapping
    public ResponseEntity<List<Ingredient>> getLimitedIngredientsByDescription(@Parameter(description = "Ingredient name like")
                                                                               @RequestParam(name = "name") String ingredientName) {
        return ResponseEntity.ok().body(ingredientService.getLimitedIngredientsByName(ingredientName));
    }
}
