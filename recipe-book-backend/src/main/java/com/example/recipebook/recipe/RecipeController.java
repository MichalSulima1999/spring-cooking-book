package com.example.recipebook.recipe;

import com.example.recipebook.recipe.dto.AddRecipeWrapper;
import com.example.recipebook.recipe.dto.RecipeGeneralInfo;
import com.example.recipebook.recipe.specification.RecipeSpecificationBuilder;
import com.example.recipebook.search.SearchCriteria;
import com.example.recipebook.search.SearchDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/recipe")
public record RecipeController(RecipeService recipeService) {

    @Operation(summary = "Get page of recipes")
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

    @Operation(summary = "Get paged dishes based on multiple search criteria")
    @PostMapping("/search")
    public ResponseEntity<Page<RecipeGeneralInfo>> searchRecipe(
            @Parameter(description = "page number")
            @RequestParam(name = "pageNum",
                    defaultValue = "0") int pageNum,
            @Parameter(description = "records per page")
            @RequestParam(name = "pageSize",
                    defaultValue = "10") int pageSize,
            @RequestBody SearchDTO
                    searchDto) {
        RecipeSpecificationBuilder builder = new
                RecipeSpecificationBuilder();
        List<SearchCriteria> criteriaList =
                searchDto.getSearchCriteriaList();
        if (criteriaList != null) {
            criteriaList.forEach(x ->
            {
                x.setDataOption(searchDto
                        .getDataOption());
                builder.with(x);
            });
        }
        Pageable page;

        if (searchDto.getOrderByField() == null) {
            page = PageRequest.of(pageNum, pageSize,
                    Sort.by("name").ascending());
        } else {
            if (searchDto.getOrderByAscending()) {
                page = PageRequest.of(pageNum, pageSize,
                        Sort.by(searchDto.getOrderByField()).ascending());
            } else {
                page = PageRequest.of(pageNum, pageSize,
                        Sort.by(searchDto.getOrderByField()).descending());
            }
        }

        Page<RecipeGeneralInfo> recipePage =
                recipeService.findBySearchCriteria(builder.build(), page);

        return ResponseEntity.ok().body(recipePage);
    }

    @Operation(summary = "Get recipe by its id")
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@Parameter(description = "id of the recipe to search") @PathVariable long id,
                                            HttpServletResponse response) {
        return ResponseEntity.ok().body(recipeService.getRecipe(id));
    }

    @Operation(summary = "Add new recipe")
    @PostMapping
    public ResponseEntity<Recipe> addRecipe(@RequestBody AddRecipeWrapper recipeWrapper) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/recipe").toUriString());
        return ResponseEntity.created(uri).body(recipeService.addRecipe(recipeWrapper));
    }

    @Operation(summary = "Add image to recipe as MultipartFile")
    @PatchMapping("/image/{id}")
    public ResponseEntity<String> editRecipeImage(@RequestParam("img") MultipartFile imageFile,
                                                  @Parameter(description = "id of the recipe")
                                                  @PathVariable("id") long recipeId,
                                                  HttpServletResponse response) throws IOException {
        if (imageFile.isEmpty()) {
            response.getWriter().write("No image found");
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return null;
        }

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/recipe/image/{id}").toUriString());
        return ResponseEntity.created(uri).body(recipeService.editRecipeImage(recipeId, imageFile));
    }
}
