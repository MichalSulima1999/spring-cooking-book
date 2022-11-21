package com.example.recipebook.ingredient;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IngredientServiceTest {
    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientRepo ingredientRepo;

    @BeforeAll
    private void setup() {
        List<Ingredient> ingredients = new ArrayList<>(Arrays.asList(
                new Ingredient("Carrot"), new Ingredient("Onion"),
                new Ingredient("Pork"), new Ingredient("Minced pork"),
                new Ingredient("Cottage cheese"), new Ingredient("Cheese")));

        ingredientRepo.saveAll(ingredients);
    }

    @AfterAll
    private void clearDb() {
        ingredientRepo.deleteAll();
    }

    @Test
    void getLimitedIngredientsByName() {
        assertEquals(2, ingredientService
                .getLimitedIngredientsByName("Cheese").size(), "Should find 2 ingredients");
        assertEquals(1, ingredientService
                .getLimitedIngredientsByName("Cottage cheese").size(), "Should find 1 ingredient");
        assertEquals(2, ingredientService
                .getLimitedIngredientsByName("POrK").size(), "Should find 2 ingredients");
    }
}