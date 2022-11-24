package com.example.recipebook.category;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepo categoryRepo;

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
    void getLimitedCategoriesByDescription() throws Exception {
        mockMvc.perform(get("/api/category").param("name", "unch"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", containsStringIgnoringCase("unch")))
                .andExpect(jsonPath("$[1].name", containsStringIgnoringCase("unch")));
    }
}