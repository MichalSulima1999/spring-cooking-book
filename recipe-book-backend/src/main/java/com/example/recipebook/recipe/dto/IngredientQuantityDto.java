package com.example.recipebook.recipe.dto;

import com.example.recipebook.ingredient.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientQuantityDto {
    private Ingredient ingredient;
    private String quantity;
}
