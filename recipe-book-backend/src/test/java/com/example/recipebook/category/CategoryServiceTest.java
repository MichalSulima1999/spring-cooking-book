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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryServiceTest {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private CategoryService categoryService;

    @BeforeAll
    private void setup() {
        List<Category> categories = new ArrayList<>(Arrays.asList(
                new Category("Breakfast"), new Category("Lunch"),
                new Category("Dinner"), new Category("Supper"),
                new Category("Dessert"), new Category("Brunch")));

        categoryRepo.saveAll(categories);
    }

    @AfterAll
    private void clearDb() {
        categoryRepo.deleteAll();
    }

    @Test
    void getLimitedCategoriesByName() {
        assertEquals(2, categoryService
                .getLimitedCategoriesByName("unch").size(), "Should find 2 categories");
        assertEquals(1, categoryService
                .getLimitedCategoriesByName("Lunch").size(), "Should find 1 category");
        assertEquals(1, categoryService
                .getLimitedCategoriesByName("dINnEr").size(), "Should find 1 category");
    }
}