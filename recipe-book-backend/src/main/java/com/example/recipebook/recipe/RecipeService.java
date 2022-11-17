package com.example.recipebook.recipe;

import com.example.recipebook.category.Category;
import com.example.recipebook.category.CategoryRepo;
import com.example.recipebook.diet.Diet;
import com.example.recipebook.diet.DietRepo;
import com.example.recipebook.ingredient.Ingredient;
import com.example.recipebook.ingredient.IngredientRepo;
import com.example.recipebook.recipe.dto.AddRecipeWrapper;
import com.example.recipebook.recipe.dto.IngredientQuantityDto;
import com.example.recipebook.recipe_ingredient.RecipeIngredient;
import com.example.recipebook.step.Step;
import com.example.recipebook.step.StepRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepo recipeRepo;
    private final CategoryRepo categoryRepo;
    private final DietRepo dietRepo;
    private final IngredientRepo ingredientRepo;
    private final StepRepo stepRepo;

    public Page<Recipe> getRecipePage(Pageable pageable) {
        return recipeRepo.findAll(pageable);
    }

    @Transactional
    public Recipe addRecipe(AddRecipeWrapper recipeWrapper) {
        Recipe recipe = recipeWrapper.getRecipe();

        Category category = recipeWrapper.getCategory();
        setCategoryInRecipe(category, recipe);

        Diet diet = recipeWrapper.getDiet();
        setDietInRecipe(diet, recipe);

        List<IngredientQuantityDto> ingredientQuantityDtos = recipeWrapper.getIngredients();
        setIngredientsInRecipe(ingredientQuantityDtos, recipe);

        Recipe newRecipe = recipeRepo.save(recipe);
        List<Step> steps = recipeWrapper.getSteps();
        setStepsInRecipe(steps, newRecipe);
        
        return newRecipe;
    }

    private void setCategoryInRecipe(Category category, Recipe recipe) {
        Optional<Category> optionalCategory = categoryRepo.findByName(category.getName());
        if (optionalCategory.isPresent()) {
            recipe.setCategory(optionalCategory.get());
        } else {
            Category newCategory = categoryRepo.save(category);
            recipe.setCategory(newCategory);
        }
    }

    private void setDietInRecipe(Diet diet, Recipe recipe) {
        Optional<Diet> optionalDiet = dietRepo.findByName(diet.getName());
        if (optionalDiet.isPresent()) {
            recipe.setDiet(optionalDiet.get());
        } else {
            Diet newDiet = dietRepo.save(diet);
            recipe.setDiet(newDiet);
        }
    }

    private void setIngredientsInRecipe(List<IngredientQuantityDto> ingredientQuantityDtos, Recipe recipe) {
        for (IngredientQuantityDto ingredientQuantityDto :
                ingredientQuantityDtos) {
            Optional<Ingredient> optionalIngredient = ingredientRepo.findByName(ingredientQuantityDto.getIngredient().getName());
            if (optionalIngredient.isPresent()) {
                RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, optionalIngredient.get(), ingredientQuantityDto.getQuantity());
                recipe.addRecipeIngredient(recipeIngredient);
            } else {
                Ingredient newIngredient = ingredientRepo.save(ingredientQuantityDto.getIngredient());
                RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, newIngredient, ingredientQuantityDto.getQuantity());
                recipe.addRecipeIngredient(recipeIngredient);
            }
        }
    }

    private void setStepsInRecipe(List<Step> steps, Recipe recipe) {
        steps = steps.stream().peek(step -> step.setRecipe(recipe)).toList();
        List<Step> savedSteps = stepRepo.saveAll(steps);
        recipe.setSteps(new HashSet<>(savedSteps));
    }
}
