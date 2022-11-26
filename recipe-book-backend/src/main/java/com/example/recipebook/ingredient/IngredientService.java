package com.example.recipebook.ingredient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepo ingredientRepo;

    public List<Ingredient> getLimitedIngredientsByName(String name) {
        return ingredientRepo.findTop5ByNameContainsIgnoreCase(name);
    }
}
