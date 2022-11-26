package com.example.recipebook.ingredient;

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
class IngredientControllerTest {
    @Autowired
    private MockMvc mockMvc;

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
    void getLimitedIngredientsByDescription() throws Exception {
        mockMvc.perform(get("/api/ingredient").param("name", "cheese"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", containsStringIgnoringCase("cheese")))
                .andExpect(jsonPath("$[1].name", containsStringIgnoringCase("cheese")));
    }
}