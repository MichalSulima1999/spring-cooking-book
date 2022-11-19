package com.example.recipebook.recipe;

import com.example.recipebook.category.Category;
import com.example.recipebook.diet.Diet;
import com.example.recipebook.ingredient.Ingredient;
import com.example.recipebook.recipe.dto.AddRecipeWrapper;
import com.example.recipebook.recipe.dto.IngredientQuantityDto;
import com.example.recipebook.recipe.dto.StepNumberDto;
import com.example.recipebook.recipe_ingredient.RecipeIngredient;
import com.example.recipebook.recipe_step.RecipeStep;
import com.example.recipebook.step.Step;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    private RecipeService recipeService;

    @BeforeAll
    private void setup() {
        Recipe recipe = new Recipe(0L, "Recipe", "Recipe desc",
                "image", 60, SkillLevel.EASY,
                null, null, new HashSet<>(), new HashSet<>());
        Category category = new Category(0L, "Category");
        Diet diet = new Diet(0L, "Diet");
        List<IngredientQuantityDto> ingredientQuantityDtos = Arrays.asList(
                new IngredientQuantityDto(new Ingredient(0L, "Ingr 1", null), "250g"),
                new IngredientQuantityDto(new Ingredient(0L, "Ingr 2", null), "260g"));
        List<StepNumberDto> stepNumberDtos = Arrays.asList(
                new StepNumberDto(new Step(0L, "Step 1 desc", null), 1),
                new StepNumberDto(new Step(0L, "Step 2 desc", null), 2));

        AddRecipeWrapper addRecipeWrapper = new AddRecipeWrapper(recipe, category, diet, ingredientQuantityDtos, stepNumberDtos);
        testRecipe = recipeService.addRecipe(addRecipeWrapper);

        for (int i = 0; i < 10; i++) {
            Recipe recipe1 = new Recipe(0L, "Recipe " + i, "Recipe " + i + " desc",
                    "image", 60, SkillLevel.EASY,
                    null, null, new HashSet<>(), new HashSet<>());
            Category category1 = new Category(0L, "Category " + i);
            Diet diet1 = new Diet(0L, "Diet " + i);
            List<IngredientQuantityDto> ingredientQuantityDtos1 = Arrays.asList(
                    new IngredientQuantityDto(new Ingredient(0L, "Ingr 1 " + i, null), "250g"),
                    new IngredientQuantityDto(new Ingredient(0L, "Ingr 2" + i, null), "260g"));
            List<StepNumberDto> stepNumberDtos1 = Arrays.asList(
                    new StepNumberDto(new Step(0L, "Step 1 desc " + i, null), 1),
                    new StepNumberDto(new Step(0L, "Step 2 desc " + i, null), 2));

            AddRecipeWrapper addRecipeWrapper1 = new AddRecipeWrapper(recipe1, category1, diet1, ingredientQuantityDtos1, stepNumberDtos1);
            recipeService.addRecipe(addRecipeWrapper1);
        }
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
                new IngredientQuantityDto(new Ingredient(0L, "Ingr 1", null), "250g"),
                new IngredientQuantityDto(new Ingredient(0L, "Ingr 2", null), "260g"));
        List<StepNumberDto> stepNumberDtos = Arrays.asList(
                new StepNumberDto(new Step(0L, "Step 1 desc", null), 1),
                new StepNumberDto(new Step(0L, "Step 2 desc", null), 2));

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
            assertTrue(recipeIngredient.getIngredient().getId() < 3L,
                    "Ingredients should not duplicate");
        }

        assertTrue(rec.getRecipeSteps().stream().findFirst().isPresent(),
                "Added recipe should have steps");

        for (RecipeStep recipeStep :
                rec.getRecipeSteps()
        ) {
            assertTrue(recipeStep.getStep().getId() < 3L,
                    "Steps should not duplicate");
        }

        assertEquals(1L, rec.getCategory().getId(),
                "Categories should not duplicate");

        assertEquals(1L, rec.getDiet().getId(),
                "Diets should not duplicate");
    }
}