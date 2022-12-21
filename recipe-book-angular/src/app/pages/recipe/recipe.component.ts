import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Recipe } from 'src/app/interfaces/Recipe';
import { RecipeDetails } from 'src/app/interfaces/RecipeDetails';
import { RecipeService } from 'src/app/services/recipe.service';

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.css'],
})
export class RecipeComponent implements OnInit {
  recipe!: RecipeDetails;
  recipeId!: number;
  isLoading: boolean = true;

  constructor(
    private recipeService: RecipeService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((param) => {
      this.recipeId = param['id'];
    });

    this.retrieveRecipe();
  }

  retrieveRecipe(): void {
    this.recipeService
      .getRecipe(this.recipeId)
      .subscribe((recipe: RecipeDetails) => {
        this.recipe = recipe;
        this.isLoading = false;
      });
  }

  getRecipePath(): string {
    if (this.recipe.image) {
      return `http://localhost:8080/api/recipe/image/${this.recipe.image}`;
    }
    return '/assets/img/food.png';
  }
}
