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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DietServiceTest {
    @Autowired
    private DietService dietService;

    @Autowired
    private DietRepo dietRepo;

    @BeforeAll
    private void setup() {
        List<Diet> diets = new ArrayList<>(Arrays.asList(
                new Diet("Vegan"), new Diet("Vegetarian"),
                new Diet("Keto"), new Diet("Paleo"),
                new Diet("Ultra-Low-Fat"), new Diet("Low-Carb")));

        dietRepo.saveAll(diets);
    }

    @AfterAll
    private void clearDb() {
        dietRepo.deleteAll();
    }

    @Test
    void getLimitedDietsByName() {
        assertEquals(2, dietService
                .getLimitedDietsByName("Low").size(), "Should find 2 diets");
        assertEquals(1, dietService
                .getLimitedDietsByName("Vegan").size(), "Should find 1 diet");
        assertEquals(2, dietService
                .getLimitedDietsByName("veG").size(), "Should find 2 diets");
    }

    @Test
    void getAllDiets() {
        assertEquals(6, dietService.getAllDiets().size(), "Should return all diets");
        assertEquals("Keto", dietService.getAllDiets().get(0).getName(), "Diet names should match");
    }
}