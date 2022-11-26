package com.example.recipebook.diet;

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
class DietRepoTest {
    @Autowired
    private DietRepo dietRepo;

    private Diet testDiet;

    @BeforeAll
    private void setup() {
        List<Diet> diets = new ArrayList<>(Arrays.asList(
                new Diet("Vegan"), new Diet("Vegetarian"),
                new Diet("Keto"), new Diet("Paleo"),
                new Diet("Ultra-Low-Fat"), new Diet("Low-Carb")));

        dietRepo.saveAll(diets);
        testDiet = dietRepo.save(new Diet("Mediterranean"));
    }

    @AfterAll
    private void clearDb() {
        dietRepo.deleteAll();
    }

    @Test
    void findByName() {
        assertTrue(dietRepo.findByName("Mediterranean").isPresent(),
                "Diet should be found");
        assertEquals(testDiet.getId(), dietRepo.findByName("Mediterranean").get().getId(),
                "Diet ids should be equal");
        assertFalse(dietRepo.findByName("Gluten-free").isPresent(),
                "Diet should not be found");
    }

    @Test
    void findTop5ByNameContainsIgnoreCase() {
        assertEquals(2, dietRepo
                .findTop5ByNameContainsIgnoreCase("Low").size(), "Should find 2 diets");
        assertEquals(1, dietRepo
                .findTop5ByNameContainsIgnoreCase("Vegan").size(), "Should find 1 diet");
        assertEquals(2, dietRepo
                .findTop5ByNameContainsIgnoreCase("veG").size(), "Should find 2 diets");
    }
}