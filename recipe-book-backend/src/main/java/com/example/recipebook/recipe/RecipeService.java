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
import com.example.recipebook.recipe_ingredient.RecipeIngredient;
import com.example.recipebook.recipe_step.RecipeStep;
import com.example.recipebook.step.Step;
import com.example.recipebook.step.StepRepo;
import com.example.recipebook.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {
    private final RecipeRepo recipeRepo;
    private final CategoryRepo categoryRepo;
    private final DietRepo dietRepo;
    private final IngredientRepo ingredientRepo;
    private final StepRepo stepRepo;
    private final AppProperties appProperties;

    public Page<Recipe> getRecipePage(Pageable pageable) {
        return recipeRepo.findAll(pageable);
    }

    public Recipe getRecipe(Long recipeId) {
        return recipeRepo.findById(recipeId).orElse(null);
    }

    public Page<RecipeGeneralInfo> findBySearchCriteria
            (Specification<Recipe> spec, Pageable page) {
        return recipeRepo.findAll(spec, RecipeGeneralInfo.class, page);
    }

    @Transactional
    public Recipe addRecipe(AddRecipeWrapper recipeWrapper) {
        Recipe recipe = recipeWrapper.getRecipe();

        Category category = recipeWrapper.getCategory();
        setCategoryInRecipe(category, recipe);

        Diet diet = recipeWrapper.getDiet();
        setDietInRecipe(diet, recipe);

        List<IngredientQuantityDto> ingredientQuantityDtos = recipeWrapper.getIngredientQuantityDtos();
        setIngredientsInRecipe(ingredientQuantityDtos, recipe);

        Recipe newRecipe = recipeRepo.save(recipe);
        List<StepNumberDto> stepNumberDtos = recipeWrapper.getStepNumberDtos();
        setStepsInRecipe(stepNumberDtos, newRecipe);

        return newRecipe;
    }

    public String editRecipeImage(Long recipeId, MultipartFile imageFile) throws IOException {
        Optional<Recipe> optionalRecipe = recipeRepo.findById(recipeId);

        if (optionalRecipe.isEmpty()) {
            return null;
        }

        String fileExtension = Objects.requireNonNull(imageFile.getOriginalFilename())
                .substring(imageFile.getOriginalFilename().lastIndexOf('.'));

        String uploadDir = appProperties.getDirectories().getRecipe();
        log.info(uploadDir);
        String fileName;
        if (optionalRecipe.get().getImage() == null || optionalRecipe.get().getImage().equals("")) {
            fileName = UUID.randomUUID() + fileExtension;
        } else {
            FileUploadUtil.deleteFile(uploadDir, optionalRecipe.get().getImage());
            String imageName = optionalRecipe.get().getImage()
                    .substring(0, optionalRecipe.get().getImage().lastIndexOf('.'));
            fileName = imageName + fileExtension;
        }

        FileUploadUtil.saveFile(uploadDir, fileName, imageFile);

        optionalRecipe.get().setImage(fileName);
        Recipe savedRecipe = recipeRepo.save(optionalRecipe.get());
        return savedRecipe.getImage();
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
            Optional<Ingredient> optionalIngredient = ingredientRepo.findByName(ingredientQuantityDto.getIngredientName());
            if (optionalIngredient.isPresent()) {
                RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, optionalIngredient.get(), ingredientQuantityDto.getQuantity());
                recipe.addRecipeIngredient(recipeIngredient);
            } else {
                Ingredient newIngredient = ingredientRepo.save(new Ingredient(ingredientQuantityDto.getIngredientName()));
                RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, newIngredient, ingredientQuantityDto.getQuantity());
                recipe.addRecipeIngredient(recipeIngredient);
            }
        }
    }

    private void setStepsInRecipe(List<StepNumberDto> stepNumberDtos, Recipe recipe) {
        for (StepNumberDto stepNumberDto :
                stepNumberDtos) {
            Optional<Step> optionalStep = stepRepo.findByDescription(stepNumberDto.getStepDescription());
            if (optionalStep.isPresent()) {
                RecipeStep recipeStep = new RecipeStep(recipe, optionalStep.get(), stepNumberDto.getStepNumber());
                recipe.addRecipeStep(recipeStep);
            } else {
                Step newStep = stepRepo.save(new Step(stepNumberDto.getStepDescription()));
                RecipeStep recipeStep = new RecipeStep(recipe, newStep, stepNumberDto.getStepNumber());
                recipe.addRecipeStep(recipeStep);
            }
        }
    }
}
