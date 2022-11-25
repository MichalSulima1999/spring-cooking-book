package com.example.recipebook.recipe;

import com.example.recipebook.category.Category;
import com.example.recipebook.category.CategoryRepo;
import com.example.recipebook.configs.AppProperties;
import com.example.recipebook.diet.Diet;
import com.example.recipebook.diet.DietRepo;
import com.example.recipebook.ingredient.Ingredient;
import com.example.recipebook.ingredient.IngredientRepo;
import com.example.recipebook.recipe.dto.AddRecipeWrapper;
import com.example.recipebook.recipe.dto.IngredientQuantityDto;
import com.example.recipebook.recipe.dto.StepNumberDto;
import com.example.recipebook.recipe_ingredient.RecipeIngredient;
import com.example.recipebook.recipe_step.RecipeStep;
import com.example.recipebook.search.SearchCriteria;
import com.example.recipebook.search.SearchDTO;
import com.example.recipebook.step.Step;
import com.example.recipebook.step.StepRepo;
import com.example.recipebook.utils.FileUploadUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecipeControllerTest {

    private final String testFileSaveDirectory = "recipe-images/";
    private final String testFileName = "testFile.png";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RecipeService service;
    @Autowired
    private RecipeRepo recipeRepo;
    @Autowired
    private IngredientRepo ingredientRepo;
    @Autowired
    private StepRepo stepRepo;
    @Autowired
    private DietRepo dietRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private AppProperties appProperties;
    private Recipe testRecipe;

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
                    testFileName, 60, SkillLevel.EASY,
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
            testRecipe = service.addRecipe(addRecipeWrapper);
        }
    }

    @AfterAll
    private void clearDb() {
        recipeRepo.deleteAll();
        stepRepo.deleteAll();
        dietRepo.deleteAll();
        ingredientRepo.deleteAll();
        categoryRepo.deleteAll();
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
                .andExpect(jsonPath("$.content[0].name", is("Recipe 0")))
                .andExpect(jsonPath("$.content[0].description", is("Recipe 0 desc")))
                .andExpect(jsonPath("$.content[0].category.name", is("Category 0")))
                .andExpect(jsonPath("$.content[1].name", is("Recipe 1")))
                .andExpect(jsonPath("$.content[1].description", is("Recipe 1 desc")))
                .andExpect(jsonPath("$.content[1].category.name", is("Category 1")))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.size", is(5)))
                .andExpect(jsonPath("$.totalElements", is(10)))
                .andExpect(jsonPath("$.number", is(0)));
    }

    @Test
    void getRecipe() throws Exception {
        mockMvc.perform(get("/api/recipe/{id}", testRecipe.getId()))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(testRecipe.getId().intValue())))
                .andExpect(jsonPath("$.name", is(testRecipe.getName())))
                .andExpect(jsonPath("$.description", is(testRecipe.getDescription())))
                .andExpect(jsonPath("$.category.name", is(testRecipe.getCategory().getName())));
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

                .andExpect(jsonPath("$.name", is("Recipe")))
                .andExpect(jsonPath("$.description", is("Recipe desc")))
                .andExpect(jsonPath("$.category.name", is(category.getName())));
    }

    @Test
    void editRecipeImage() throws Exception {
        testRecipe.setImage(testFileName);
        MockMultipartFile file
                = new MockMultipartFile(
                "img",
                testFileName,
                MediaType.IMAGE_PNG_VALUE,
                new byte[1]
        );

        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/recipe/image/{id}", testRecipe.getId()).file(file))
                // Validate the response code and content type
                .andExpect(status().isCreated())

                // Validate the returned fields
                .andExpect(jsonPath("$", is(testRecipe.getImage())));

        FileUploadUtil.deleteFile(appProperties.getDirectories().getRecipe(), testRecipe.getImage());
    }

    @Test
    void searchRecipe() throws Exception {
        // Setup
        Category category1 = new Category(0L, "Salad");
        Category savedCategory1 = categoryRepo.save(category1);
        Diet diet1 = new Diet(0L, "Vegan");
        Diet savedDiet1 = dietRepo.save(diet1);

        Category category2 = new Category(0L, "Dessert");
        Category savedCategory2 = categoryRepo.save(category2);
        Diet diet2 = new Diet(0L, "Vegetarian");
        Diet savedDiet2 = dietRepo.save(diet2);

        Recipe recipe1 = new Recipe(0L, "Pumpkin pie", "Pie desc",
                null, 60, SkillLevel.EASY,
                savedCategory2, savedDiet2, null, null);

        Recipe recipe2 = new Recipe(0L, "Spring salad", "Spring salad desc",
                null, 20, SkillLevel.MEDIUM,
                savedCategory1, savedDiet1, null, null);

        Recipe recipe3 = new Recipe(0L, "Summer salad", "Summer salad desc",
                null, 15, SkillLevel.EASY,
                savedCategory1, savedDiet1, null, null);

        List<Ingredient> savedIngredients1 = ingredientRepo.saveAll(Arrays.asList(
                new Ingredient("Ingr 3"), new Ingredient("Ingr 4")
        ));
        List<RecipeIngredient> recipeIngredients1 = savedIngredients1.stream().map(ingredient ->
                new RecipeIngredient(recipe1, ingredient, "250g")).toList();
        recipe1.setRecipeIngredients(new HashSet<>(recipeIngredients1));
        List<RecipeIngredient> recipeIngredients2 = savedIngredients1.stream().map(ingredient ->
                new RecipeIngredient(recipe2, ingredient, "250g")).toList();
        recipe2.setRecipeIngredients(new HashSet<>(recipeIngredients2));
        List<RecipeIngredient> recipeIngredients3 = savedIngredients1.stream().map(ingredient ->
                new RecipeIngredient(recipe3, ingredient, "250g")).toList();
        recipe3.setRecipeIngredients(new HashSet<>(recipeIngredients3));

        List<Step> savedSteps1 = stepRepo.saveAll(Arrays.asList(
                new Step(0L, "Step 3 desc", new HashSet<>()), new Step(0L, "Step 4 desc", new HashSet<>())
        ));
        List<RecipeStep> recipeSteps1 = savedSteps1.stream().map((step) ->
                new RecipeStep(recipe1, step, step.getId().intValue() + 1)).toList();
        recipe1.setRecipeSteps(new HashSet<>(recipeSteps1));
        List<RecipeStep> recipeSteps2 = savedSteps1.stream().map((step) ->
                new RecipeStep(recipe2, step, step.getId().intValue() + 1)).toList();
        recipe2.setRecipeSteps(new HashSet<>(recipeSteps2));
        List<RecipeStep> recipeSteps3 = savedSteps1.stream().map((step) ->
                new RecipeStep(recipe3, step, step.getId().intValue() + 1)).toList();
        recipe3.setRecipeSteps(new HashSet<>(recipeSteps3));

        recipeRepo.saveAll(Arrays.asList(recipe1, recipe2, recipe3));

        // Test
        List<SearchCriteria> searchCriteria = List.of(
                new SearchCriteria("name", "cn", "salad"),
                new SearchCriteria("cookingMinutes", "le", "20"),
                new SearchCriteria("cookingMinutes", "gt", "10")
        );
        SearchDTO searchDTO = new SearchDTO(searchCriteria, "all", "name", true);

        mockMvc.perform(post("/api/recipe/search")
                        .param("pageNum", "0")
                        .param("pageSize", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(searchDTO)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name", is("Spring salad")))
                .andExpect(jsonPath("$.content[1].name", is("Summer salad")));
    }

    @Test
    void editRecipe() throws Exception {
        Recipe recipe = new Recipe(0L, "Edited recipe", "Edited recipe desc",
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

        mockMvc.perform(put("/api/recipe/{id}", testRecipe.getId())

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addRecipeWrapper)))

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(testRecipe.getId().intValue())))
                .andExpect(jsonPath("$.name", is(recipe.getName())));
    }

    @Test
    void getImageWithMediaType() throws Exception {
        testRecipe.setImage(testFileName);
        MockMultipartFile file
                = new MockMultipartFile(
                testFileName,
                testFileName,
                MediaType.IMAGE_PNG_VALUE,
                new byte[1]
        );

        FileUploadUtil.saveFile(testFileSaveDirectory, testFileName, file);

        mockMvc.perform(get("/api/recipe/image/{imageName}", testRecipe.getImage())

                        .contentType(MediaType.IMAGE_PNG_VALUE))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE));

        FileUploadUtil.deleteFile(testFileSaveDirectory, testFileName);
    }
}