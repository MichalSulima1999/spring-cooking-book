package com.example.recipebook.recipe.dto;

import com.example.recipebook.category.Category;
import com.example.recipebook.diet.Diet;
import com.example.recipebook.recipe.Recipe;
import lombok.Data;

import java.util.List;

@Data
public class AddRecipeWrapper {
    private Recipe recipe;
    private Category category;
    private Diet diet;
    private List<IngredientQuantityDto> ingredientQuantityDtos;
    private List<StepNumberDto> stepNumberDtos;
}
