package com.example.recipebook.diet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/diet")
public record DietController(DietService dietService) {
    @Operation(summary = "Get a few diets by name")
    @GetMapping
    public ResponseEntity<List<Diet>> getLimitedDietsByDescription(@Parameter(description = "Diet name like")
                                                                   @RequestParam(name = "name") String dietName) {
        return ResponseEntity.ok().body(dietService.getLimitedDietsByName(dietName));
    }
}
