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
import com.example.recipebook.recipe.dto.RecipeGeneralInfo;
import com.example.recipebook.recipe.dto.StepNumberDto;
import com.example.recipebook.recipe.specification.RecipeSpecificationBuilder;
import com.example.recipebook.recipe_ingredient.RecipeIngredient;
import com.example.recipebook.recipe_step.RecipeStep;
import com.example.recipebook.search.SearchCriteria;
import com.example.recipebook.search.SearchDTO;
import com.example.recipebook.step.Step;
import com.example.recipebook.step.StepRepo;
import com.example.recipebook.utils.FileUploadUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
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
    private CategoryRepo categoryRepo;

    @Autowired
    private AppProperties appProperties;

    @BeforeAll
    private void setup() {
        Category category = new Category(0L, "Category");
        Category savedCategory = categoryRepo.save(category);
        Diet diet = new Diet(0L, "Diet");
        Diet savedDiet = dietRepo.save(diet);

        Recipe recipe = new Recipe(0L, "Recipe", "Recipe desc",
                null, 60, SkillLevel.EASY,
                savedCategory, savedDiet, null, null);

        List<Ingredient> savedIngredients = ingredientRepo.saveAll(Arrays.asList(
                new Ingredient("Ingr 1"), new Ingredient("Ingr 2")
        ));
        List<RecipeIngredient> recipeIngredients = savedIngredients.stream().map(ingredient ->
                new RecipeIngredient(recipe, ingredient, "250g")).toList();
        recipe.setRecipeIngredients(new HashSet<>(recipeIngredients));

        List<Step> savedSteps = stepRepo.saveAll(Arrays.asList(
                new Step(0L, "Step 1 desc", new HashSet<>()), new Step(0L, "Step 2 desc", new HashSet<>())
        ));
        List<RecipeStep> recipeSteps = savedSteps.stream().map((step) ->
                new RecipeStep(recipe, step, step.getId().intValue() + 1)).toList();
        recipe.setRecipeSteps(new HashSet<>(recipeSteps));

        testRecipe = recipeRepo.save(recipe);

        for (int i = 0; i < 10; i++) {
            Category category1 = new Category(0L, "Category " + i);
            Category savedCategory1 = categoryRepo.save(category1);
            Diet diet1 = new Diet(0L, "Diet " + i);
            Diet savedDiet1 = dietRepo.save(diet1);

            Recipe recipe1 = new Recipe(0L, "Recipe " + i, "Recipe desc " + i,
                    null, 60, SkillLevel.EASY,
                    savedCategory1, savedDiet1, null, null);

            List<Ingredient> savedIngredients1 = ingredientRepo.saveAll(Arrays.asList(
                    new Ingredient("Ingr 1 " + i), new Ingredient("Ingr 2 " + i)
            ));
            List<RecipeIngredient> recipeIngredients1 = savedIngredients1.stream().map(ingredient ->
                    new RecipeIngredient(recipe1, ingredient, "250g")).toList();
            recipe.setRecipeIngredients(new HashSet<>(recipeIngredients1));

            List<Step> savedSteps1 = stepRepo.saveAll(Arrays.asList(
                    new Step(0L, "Step 1 desc " + i, new HashSet<>()), new Step(0L, "Step 2 desc " + i, new HashSet<>())
            ));
            List<RecipeStep> recipeSteps1 = savedSteps1.stream().map((step) ->
                    new RecipeStep(recipe1, step, step.getId().intValue() + 1)).toList();
            recipe.setRecipeSteps(new HashSet<>(recipeSteps1));

            recipeRepo.save(recipe1);
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
    void getRecipePage() {
        Pageable page = PageRequest.of(0, 4,
                Sort.by("name").ascending());
        assertEquals(11, recipeService.getRecipePage(page).getTotalElements(), "Should be 11 recipes");
    }

    @Test
    void getRecipe() {
        assertEquals(testRecipe.getName(), recipeService.getRecipe(testRecipe.getId()).getName(), "Should return correct recipe");
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

        String editedRecipeImageName = recipeService.editRecipeImage(testRecipe.getId(), file);
        Recipe editedRecipe = recipeService.getRecipe(testRecipe.getId());

        FileUploadUtil.deleteFile(appProperties.getDirectories().getRecipe(), editedRecipeImageName);

        assertEquals(editedRecipe.getImage(), editedRecipeImageName);
    }

    @Test
    void findBySearchCriteria() {
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
                new SearchCriteria("cookingMinutes", "lt", "30")
        );
        SearchDTO searchDTO = new SearchDTO(searchCriteria, "all", "name", true);
        RecipeSpecificationBuilder builder = new
                RecipeSpecificationBuilder();

        List<SearchCriteria> criteriaList =
                searchDTO.getSearchCriteriaList();
        criteriaList.forEach(x ->
        {
            x.setDataOption(searchDTO
                    .getDataOption());
            builder.with(x);
        });

        Pageable page = PageRequest.of(0, 5,
                Sort.by(searchDTO.getOrderByField()).ascending());

        Page<RecipeGeneralInfo> recipePage = recipeService.findBySearchCriteria(builder.build(), page);
        assertEquals(2, recipePage.getTotalElements());

        // Test 2
        List<SearchCriteria> searchCriteria2 = List.of(
                new SearchCriteria("category", "eq", "salad"),
                new SearchCriteria("name", "ne", "Summer salad")
        );
        SearchDTO searchDTO2 = new SearchDTO(searchCriteria2, "all", "name", true);
        RecipeSpecificationBuilder builder2 = new
                RecipeSpecificationBuilder();

        List<SearchCriteria> criteriaList2 =
                searchDTO2.getSearchCriteriaList();
        criteriaList2.forEach(x ->
        {
            x.setDataOption(searchDTO2
                    .getDataOption());
            builder2.with(x);
        });

        Pageable page2 = PageRequest.of(0, 5,
                Sort.by(searchDTO2.getOrderByField()).ascending());

        Page<RecipeGeneralInfo> recipePage2 = recipeService.findBySearchCriteria(builder2.build(), page2);
        assertEquals(0, recipePage2.getTotalElements());
    }

    @Test
    void editRecipe() {
        // Setup
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

        // Test
        Recipe editedRecipe = recipeService.editRecipe(addRecipeWrapper, testRecipe.getId());
        assertEquals(testRecipe.getId(), editedRecipe.getId());
        assertEquals(recipe.getName(), editedRecipe.getName());
    }
}