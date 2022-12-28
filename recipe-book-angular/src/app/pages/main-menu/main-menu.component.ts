import { Component, OnInit } from '@angular/core';
import { Recipe } from 'src/app/interfaces/Recipe';
import { RecipeService } from 'src/app/services/recipe.service';
import { PageEvent } from '@angular/material/paginator';
import { RecipePage } from 'src/app/interfaces/RecipePage';

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.css'],
})
export class MainMenuComponent implements OnInit {
  recipes: Recipe[] = [];
  pageEvent!: PageEvent;

  page = 0;
  count = 0;
  pageSize = 9;

  constructor(private recipeService: RecipeService) {}

  ngOnInit(): void {
    this.retrieveRecipes();
  }

  retrieveRecipes(): void {
    this.recipeService
      .getRecipes(this.page, this.pageSize)
      .subscribe((recipePage: RecipePage) => {
        this.recipes = recipePage.content;
        this.count = recipePage.totalElements;
      });
  }
}
