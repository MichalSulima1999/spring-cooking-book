package com.example.recipebook.recipe;

import com.example.recipebook.recipe.dto.AddRecipeWrapper;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/recipe")
public record RecipeController(RecipeService recipeService) {
    @GetMapping
    public ResponseEntity<Page<Recipe>> getRecipePage(@Parameter(description = "page number")
                                                      @RequestParam(name = "pageNum",
                                                              defaultValue = "0") int pageNum,
                                                      @Parameter(description = "records per page")
                                                      @RequestParam(name = "pageSize",
                                                              defaultValue = "10") int pageSize) {
        Pageable page = PageRequest.of(pageNum, pageSize,
                Sort.by("name").ascending());
        return ResponseEntity.ok().body(recipeService.getRecipePage(page));
    }

    @PostMapping
    public ResponseEntity<Recipe> addRecipe(@RequestBody AddRecipeWrapper recipeWrapper) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/recipe").toUriString());
        return ResponseEntity.created(uri).body(recipeService.addRecipe(recipeWrapper));
    }
}
