package com.example.recipebook.recipe.dto;

import com.example.recipebook.category.Category;
import com.example.recipebook.diet.Diet;

public interface RecipeGeneralInfo {
    Long getId();

    String getName();

    String getDescription();

    String getImage();

    int getCookingMinutes();

    String getSkillLevel();

    Category getCategory();

    Diet getDiet();
}
