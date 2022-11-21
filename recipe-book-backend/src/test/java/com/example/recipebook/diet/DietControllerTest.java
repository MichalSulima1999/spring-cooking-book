package com.example.recipebook.diet;

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
class DietControllerTest {
    @Autowired
    private MockMvc mockMvc;

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
    void getLimitedDietsByDescription() throws Exception {
        mockMvc.perform(get("/api/diet").param("name", "Low"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", containsStringIgnoringCase("low")))
                .andExpect(jsonPath("$[1].name", containsStringIgnoringCase("low")));
    }
}