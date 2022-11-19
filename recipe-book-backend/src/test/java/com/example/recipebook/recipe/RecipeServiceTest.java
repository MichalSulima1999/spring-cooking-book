package com.example.recipebook.recipe;

import com.example.recipebook.category.Category;
import com.example.recipebook.diet.Diet;
import com.example.recipebook.ingredient.Ingredient;
import com.example.recipebook.recipe.dto.AddRecipeWrapper;
import com.example.recipebook.recipe.dto.IngredientQuantityDto;
import com.example.recipebook.recipe.dto.StepNumberDto;
import com.example.recipebook.step.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @BeforeEach
    void prepareDatabase() {
        Recipe recipe = new Recipe(0L, "Recipe 1", "Recipe 1 desc",
                "image", 60, SkillLevel.EASY,
                null, null, new HashSet<>(), new HashSet<>());
        Category category = new Category(0L, "Category 1");
        Diet diet = new Diet(0L, "Diet 1");
        List<IngredientQuantityDto> ingredientQuantityDtos = Arrays.asList(
                new IngredientQuantityDto(new Ingredient(0L, "Ingr 1", null), "250g"),
                new IngredientQuantityDto(new Ingredient(0L, "Ingr 2", null), "260g"));
        List<StepNumberDto> stepNumberDtos = Arrays.asList(
                new StepNumberDto(new Step(0L, "Step 1 desc", null), 1),
                new StepNumberDto(new Step(0L, "Step 2 desc", null), 2));

        AddRecipeWrapper addRecipeWrapper = new AddRecipeWrapper(recipe, category, diet, ingredientQuantityDtos, stepNumberDtos);
        recipeService.addRecipe(addRecipeWrapper);
    }

    @Test
    void getRecipePage() {
    }

    @Test
    void getRecipe() {
    }

    @Test
    void addRecipe() {
    }
}