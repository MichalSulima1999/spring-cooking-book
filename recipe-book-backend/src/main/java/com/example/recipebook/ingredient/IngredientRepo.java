package com.example.recipebook.ingredient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IngredientRepo extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByNameIn(Collection<String> names);

    Optional<Ingredient> findByName(String name);
}
