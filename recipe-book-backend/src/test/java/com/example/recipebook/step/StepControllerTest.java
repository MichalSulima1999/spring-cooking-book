package com.example.recipebook.step;

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
class StepControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StepRepo stepRepo;

    @BeforeAll
    private void setup() {
        List<Step> steps = new ArrayList<>(Arrays.asList(
                new Step("Chop onions"), new Step("Chop meat"),
                new Step("Chop carrots"), new Step("Glaze the onion"),
                new Step("Fry the meat"), new Step("Cook the carrot")));
        stepRepo.saveAll(steps);
    }

    @AfterAll
    private void clearDb() {
        stepRepo.deleteAll();
    }

    @Test
    void getLimitedStepsByDescription() throws Exception {
        mockMvc.perform(get("/api/step").param("description", "Chop"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].description", containsStringIgnoringCase("chop")))
                .andExpect(jsonPath("$[1].description", containsStringIgnoringCase("chop")))
                .andExpect(jsonPath("$[2].description", containsStringIgnoringCase("chop")));
    }
}