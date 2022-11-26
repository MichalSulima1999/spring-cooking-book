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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IngredientRepoTest {
    @Autowired
    private IngredientRepo ingredientRepo;

    private Ingredient testIngredient;

    @BeforeAll
    private void setup() {
        List<Ingredient> ingredients = new ArrayList<>(Arrays.asList(
                new Ingredient("Carrot"), new Ingredient("Onion"),
                new Ingredient("Pork"), new Ingredient("Minced pork"),
                new Ingredient("Cottage cheese"), new Ingredient("Cheese")));

        ingredientRepo.saveAll(ingredients);
        testIngredient = ingredientRepo.save(new Ingredient("Chicken breast"));
    }

    @AfterAll
    private void clearDb() {
        ingredientRepo.deleteAll();
    }

    @Test
    void findByName() {
        assertTrue(ingredientRepo.findByName("Chicken breast").isPresent(),
                "Ingredient should be found");
        assertEquals(testIngredient.getId(), ingredientRepo.findByName("Chicken breast").get().getId(),
                "Ingredient ids should be equal");
        assertFalse(ingredientRepo.findByName("Milk").isPresent(),
                "Ingredient should not be found");
    }

    @Test
    void findTop5ByNameContainsIgnoreCase() {
        assertEquals(2, ingredientRepo
                .findTop5ByNameContainsIgnoreCase("Cheese").size(), "Should find 2 ingredients");
        assertEquals(1, ingredientRepo
                .findTop5ByNameContainsIgnoreCase("Cottage cheese").size(), "Should find 1 ingredient");
        assertEquals(2, ingredientRepo
                .findTop5ByNameContainsIgnoreCase("POrK").size(), "Should find 2 ingredients");
    }
}