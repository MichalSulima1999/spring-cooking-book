package com.example.recipebook.recipe;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepo extends JpaRepository<Recipe, Long> {

}
