package com.example.recipebook.recipe.dto;

import com.example.recipebook.category.Category;
import com.example.recipebook.diet.Diet;
import com.example.recipebook.recipe.Recipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRecipeWrapper {
    private Recipe recipe;
    private Category category;
    private Diet diet;
    private List<IngredientQuantityDto> ingredientQuantityDtos;
    private List<StepNumberDto> stepNumberDtos;
}
