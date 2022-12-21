import { Component, OnInit } from '@angular/core';
import { RecipeDetails } from 'src/app/interfaces/RecipeDetails';
import { RecipeToAdd } from 'src/app/interfaces/RecipeToAdd';
import { ActivatedRoute } from '@angular/router';
import { RecipeService } from 'src/app/services/recipe.service';

@Component({
  selector: 'app-edit-recipe',
  templateUrl: './edit-recipe.component.html',
  styleUrls: ['./edit-recipe.component.css'],
})
export class EditRecipeComponent implements OnInit {
  recipe: RecipeDetails = history.state.data;
  recipeToAdd!: RecipeToAdd;
  isLoading: boolean = true;

  constructor(
    private activatedRoute: ActivatedRoute,
    private recipeService: RecipeService
  ) {}

  ngOnInit(): void {
    if (!this.recipe) {
      this.activatedRoute.params.subscribe((param) => {
        const recipeId = param['id'];
        this.recipeService
          .getRecipe(recipeId)
          .subscribe((recipe: RecipeDetails) => {
            this.castRecipeToRecipeToAdd(recipe);
          });
      });
    } else {
      this.castRecipeToRecipeToAdd(this.recipe);
    }
  }

  castRecipeToRecipeToAdd(recipe: RecipeDetails) {
    this.recipeToAdd = {
      recipe: {
        id: recipe.id,
        name: recipe.name,
        description: recipe.description,
        cookingMinutes: recipe.cookingMinutes,
        skillLevel: recipe.skillLevel,
      },
      category: {
        name: recipe.category.name,
      },
      diet: {
        name: recipe.diet.name,
      },
      ingredientQuantityDtos: recipe.recipeIngredients.map((ingredient) => ({
        ingredientName: ingredient.ingredient.name,
        quantity: ingredient.quantity,
      })),
      stepNumberDtos: recipe.recipeSteps.map((step) => ({
        stepDescription: step.step.description,
        stepNumber: step.stepNumber,
      })),
    };

    this.isLoading = false;
  }
}
