package com.example.recipebook.recipe.dto;

import com.example.recipebook.ingredient.Ingredient;
import lombok.Data;

@Data
public class IngredientQuantityDto {
    private Ingredient ingredient;
    private String quantity;
}
