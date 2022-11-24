package com.example.recipebook.recipe;

import com.example.recipebook.category.Category;
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
import com.example.recipebook.step.Step;
import com.example.recipebook.step.StepRepo;
import com.example.recipebook.utils.FileUploadUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecipeServiceTest {

    Recipe testRecipe;
    @Autowired
    EntityManager entityManager;
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepo recipeRepo;

    @Autowired
    private IngredientRepo ingredientRepo;

    @Autowired
    private StepRepo stepRepo;

    @Autowired
    private DietRepo dietRepo;

    @Autowired
    private AppProperties appProperties;

    @BeforeAll
    private void setup() {
        Recipe recipe = new Recipe(0L, "Recipe", "Recipe desc",
                null, 60, SkillLevel.EASY,
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
        testRecipe = recipeService.addRecipe(addRecipeWrapper);

        for (int i = 0; i < 10; i++) {
            Recipe recipe1 = new Recipe(0L, "Recipe " + i, "Recipe " + i + " desc",
                    "image", 60, SkillLevel.EASY,
                    null, null, new HashSet<>(), new HashSet<>());
            Category category1 = new Category(0L, "Category " + i);
            Diet diet1 = new Diet(0L, "Diet " + i);
            List<IngredientQuantityDto> ingredientQuantityDtos1 = Arrays.asList(
                    new IngredientQuantityDto("Ingr 1 " + i, "250g"),
                    new IngredientQuantityDto("Ingr 2 " + i, "260g"));
            List<StepNumberDto> stepNumberDtos1 = Arrays.asList(
                    new StepNumberDto("Step 1 desc " + i, 1),
                    new StepNumberDto("Step 2 desc " + i, 2));

            AddRecipeWrapper addRecipeWrapper1 = new AddRecipeWrapper(recipe1, category1, diet1, ingredientQuantityDtos1, stepNumberDtos1);
            recipeService.addRecipe(addRecipeWrapper1);
        }
    }

    @AfterAll
    private void clearDb() {
        recipeRepo.deleteAll();
        stepRepo.deleteAll();
        dietRepo.deleteAll();
        ingredientRepo.deleteAll();
    }

    @Test
    void getRecipePage() {
        Pageable page = PageRequest.of(0, 4,
                Sort.by("name").ascending());
        assertEquals(11, recipeService.getRecipePage(page).getTotalElements(), "Should be 11 recipes");
    }

    @Test
    void getRecipe() {
        assertEquals(testRecipe.getName(), recipeService.getRecipe(1L).getName(), "Should return correct recipe");
    }

    @Test
    void addRecipe() {
        Recipe recipe = new Recipe(0L, "New recipe", "Recipe desc",
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
        Recipe rec = recipeService.addRecipe(addRecipeWrapper);

        assertEquals(recipe.getName(), rec.getName(), "Added recipe should have the same name");

        assertEquals(recipe.getDescription(), rec.getDescription(),
                "Added recipe should have the same descriptions");

        assertEquals(recipe.getImage(), rec.getImage(),
                "Added recipe should have the same images");

        assertEquals(recipe.getCookingMinutes(), rec.getCookingMinutes(),
                "Added recipe should have the same cooking minutes");

        assertTrue(rec.getRecipeIngredients().stream().findFirst().isPresent(),
                "Added recipe should have ingredients");

        for (RecipeIngredient recipeIngredient :
                rec.getRecipeIngredients()
        ) {
            Query query = entityManager.createQuery("select i from Ingredient i where i.name = '" + recipeIngredient.getIngredient().getName() + "'");
            List<Ingredient> ingredients = query.getResultList();
            assertTrue(ingredients.size() < 2,
                    "Ingredients should not duplicate");
        }

        assertTrue(rec.getRecipeSteps().stream().findFirst().isPresent(),
                "Added recipe should have steps");

        for (RecipeStep recipeStep :
                rec.getRecipeSteps()
        ) {
            Query query = entityManager.createQuery("select s from Step s where s.description = '" + recipeStep.getStep().getDescription() + "'");
            List<Step> steps = query.getResultList();
            assertTrue(steps.size() < 2,
                    "Steps should not duplicate");
        }

        Query queryCategory = entityManager.createQuery("select c from Category c where c.name = '"
                + rec.getCategory().getName() + "'");
        List<Category> categories = queryCategory.getResultList();
        assertTrue(categories.size() < 2,
                "Categories should not duplicate");

        Query queryDiet = entityManager.createQuery("select d from Diet d where d.name = '"
                + rec.getDiet().getName() + "'");
        List<Diet> diets = queryDiet.getResultList();
        assertTrue(diets.size() < 2,
                "Diets should not duplicate");
    }

    @Test
    void editRecipeImage() throws IOException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[1]
        );

        String editedRecipeImageName = recipeService.editRecipeImage(1L, file);
        Recipe editedRecipe = recipeService.getRecipe(1L);

        FileUploadUtil.deleteFile(appProperties.getDirectories().getRecipe(), editedRecipeImageName);

        assertEquals(editedRecipe.getImage(), editedRecipeImageName);
    }
}