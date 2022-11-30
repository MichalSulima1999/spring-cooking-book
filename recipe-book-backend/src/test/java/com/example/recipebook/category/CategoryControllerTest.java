package com.example.recipebook.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
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

    @Test
    @Order(2)
    void getAllCategories() throws Exception {
        mockMvc.perform(get("/api/category/all"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].name", is("Breakfast")))
                .andExpect(jsonPath("$[1].name", is("Brunch")))
                .andExpect(jsonPath("$[2].name", is("Dessert")))
                .andExpect(jsonPath("$[3].name", is("Dinner")))
                .andExpect(jsonPath("$[4].name", is("Lunch")))
                .andExpect(jsonPath("$[5].name", is("Supper")));
    }

    @Test
    @Order(3)
    void addCategory() throws Exception {
        Category category = new Category("Test category");

        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(category)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.name", is("Test category")));
    }
}