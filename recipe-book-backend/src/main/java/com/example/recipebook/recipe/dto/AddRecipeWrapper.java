package com.example.recipebook.recipe.dto;

import com.example.recipebook.category.Category;
import com.example.recipebook.diet.Diet;
import com.example.recipebook.recipe.Recipe;
import com.example.recipebook.step.Step;
import lombok.Data;

import java.util.List;

@Data
public class AddRecipeWrapper {
    private Recipe recipe;
    private Category category;
    private Diet diet;
    private List<IngredientQuantityDto> ingredients;
    private List<Step> steps;
}
