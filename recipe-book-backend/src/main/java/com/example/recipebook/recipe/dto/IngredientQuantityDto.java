package com.example.recipebook.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientQuantityDto {
    private String ingredientName;
    private String quantity;
}
