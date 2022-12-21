import { Component, Input } from '@angular/core';
import { Recipe } from 'src/app/interfaces/Recipe';

@Component({
  selector: 'app-recipe-card',
  templateUrl: './recipe-card.component.html',
  styleUrls: ['./recipe-card.component.css'],
})
export class RecipeCardComponent {
  @Input() recipe!: Recipe;

  getRecipePath(): string {
    if (this.recipe.image) {
      return `http://localhost:8080/api/recipe/image/${this.recipe.image}`;
    }
    return '/assets/img/food.png';
  }
}
