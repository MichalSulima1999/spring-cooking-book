import { Component, Inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA } from '@angular/material/snack-bar';

@Component({
  selector: 'app-recipe-added-snackbar',
  templateUrl: './recipe-added-snackbar.component.html',
  styleUrls: ['./recipe-added-snackbar.component.css'],
})
export class RecipeAddedSnackbarComponent {
  constructor(
    @Inject(MAT_SNACK_BAR_DATA) public data: { text: string; recipeId: number }
  ) {}
}
