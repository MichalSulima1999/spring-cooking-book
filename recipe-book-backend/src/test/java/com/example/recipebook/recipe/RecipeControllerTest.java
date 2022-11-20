package com.example.recipebook.recipe;

import com.example.recipebook.category.Category;
import com.example.recipebook.diet.Diet;
import com.example.recipebook.recipe.dto.AddRecipeWrapper;
import com.example.recipebook.recipe.dto.IngredientQuantityDto;
import com.example.recipebook.recipe.dto.StepNumberDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeService service;

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    private void setup() {
        // Setup
        for (int i = 0; i < 10; i++) {
            Recipe recipe = new Recipe(0L, "Recipe " + i, "Recipe " + i + " desc",
                    "image", 60, SkillLevel.EASY,
                    null, null, new HashSet<>(), new HashSet<>());
            Category category = new Category(0L, "Category " + i);
            Diet diet = new Diet(0L, "Diet " + i);
            List<IngredientQuantityDto> ingredientQuantityDtos = Arrays.asList(
                    new IngredientQuantityDto("Ingr 1 " + i, "250g"),
                    new IngredientQuantityDto("Ingr 1 " + i, "260g"));
            List<StepNumberDto> stepNumberDtos = Arrays.asList(
                    new StepNumberDto("Step 1 desc " + i, 1),
                    new StepNumberDto("Step 2 desc " + i, 2));

            AddRecipeWrapper addRecipeWrapper = new AddRecipeWrapper(recipe, category, diet,
                    ingredientQuantityDtos, stepNumberDtos);
            service.addRecipe(addRecipeWrapper);
        }
    }

    @Test
    @DisplayName("GET /recipe success")
    void getRecipePage() throws Exception {
        mockMvc.perform(get("/api/recipe?pageNum=0&pageSize=5"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("Recipe 0")))
                .andExpect(jsonPath("$.content[0].description", is("Recipe 0 desc")))
                .andExpect(jsonPath("$.content[0].category.id", is(1)))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].name", is("Recipe 1")))
                .andExpect(jsonPath("$.content[1].description", is("Recipe 1 desc")))
                .andExpect(jsonPath("$.content[1].category.id", is(2)))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.size", is(5)))
                .andExpect(jsonPath("$.totalElements", is(10)))
                .andExpect(jsonPath("$.number", is(0)));
    }

    @Test
    void getRecipe() throws Exception {
        mockMvc.perform(get("/api/recipe/{id}", 1L))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Recipe 0")))
                .andExpect(jsonPath("$.description", is("Recipe 0 desc")))
                .andExpect(jsonPath("$.category.id", is(1)));
    }

    @Test
    void addRecipe() throws Exception {
        Recipe recipe = new Recipe(0L, "Recipe", "Recipe desc",
                "image", 60, SkillLevel.EASY,
                null, null, new HashSet<>(), new HashSet<>());
        Category category = new Category(0L, "Category");
        Diet diet = new Diet(0L, "Diet");
        List<IngredientQuantityDto> ingredientQuantityDtos = Arrays.asList(
                new IngredientQuantityDto("Ingr 1", "250g"),
                new IngredientQuantityDto("Ingr 2", "260g"));
        List<StepNumberDto> stepNumberDtos = Arrays.asList(
                new StepNumberDto("Step 1 desc", 1),
                new StepNumberDto("Step 2 desc", 2));

        AddRecipeWrapper addRecipeWrapper = new AddRecipeWrapper(recipe, category, diet, ingredientQuantityDtos, stepNumberDtos);

        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addRecipeWrapper)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(11)))
                .andExpect(jsonPath("$.name", is("Recipe")))
                .andExpect(jsonPath("$.description", is("Recipe desc")))
                .andExpect(jsonPath("$.category.id", is(11)));
    }

    @Test
    void recipeService() {
    }
}