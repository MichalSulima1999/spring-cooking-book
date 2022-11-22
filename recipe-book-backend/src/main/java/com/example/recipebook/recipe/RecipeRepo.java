package com.example.recipebook.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

public interface RecipeRepo extends JpaRepository<Recipe, Long>, JpaSpecificationExecutorWithProjection<Recipe> {

}
