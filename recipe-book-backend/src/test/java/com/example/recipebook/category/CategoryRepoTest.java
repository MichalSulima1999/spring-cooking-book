package com.example.recipebook.category;

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
class CategoryRepoTest {
    @Autowired
    private CategoryRepo categoryRepo;

    private Category testCategory;

    @BeforeAll
    private void setup() {
        List<Category> categories = new ArrayList<>(Arrays.asList(
                new Category("Breakfast"), new Category("Lunch"),
                new Category("Dinner"), new Category("Supper"),
                new Category("Dessert"), new Category("Brunch")));

        categoryRepo.saveAll(categories);
        testCategory = categoryRepo.save(new Category("Snack"));
    }

    @AfterAll
    private void clearDb() {
        categoryRepo.deleteAll();
    }

    @Test
    void findByName() {
        assertTrue(categoryRepo.findByName("Snack").isPresent(),
                "Category should be found");
        assertEquals(testCategory.getId(), categoryRepo.findByName("Snack").get().getId(),
                "Category ids should be equal");
        assertFalse(categoryRepo.findByName("Fast food").isPresent(),
                "Category should not be found");
    }

    @Test
    void findTop5ByNameContainsIgnoreCase() {
        assertEquals(2, categoryRepo
                .findTop5ByNameContainsIgnoreCase("unch").size(), "Should find 2 categories");
        assertEquals(1, categoryRepo
                .findTop5ByNameContainsIgnoreCase("Lunch").size(), "Should find 1 category");
        assertEquals(1, categoryRepo
                .findTop5ByNameContainsIgnoreCase("dINnEr").size(), "Should find 1 category");
    }
}