import { Component, Input } from '@angular/core';
import { RecipeIngredient } from 'src/app/interfaces/RecipeIngredient';

@Component({
  selector: 'app-recipe-ingredients',
  templateUrl: './recipe-ingredients.component.html',
  styleUrls: ['./recipe-ingredients.component.css'],
})
export class RecipeIngredientsComponent {
  @Input() ingredient!: RecipeIngredient;
}
