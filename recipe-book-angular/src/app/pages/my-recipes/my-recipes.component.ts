import { Component, OnInit } from '@angular/core';
import { Recipe } from 'src/app/interfaces/Recipe';
import { RecipeService } from 'src/app/services/recipe.service';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-my-recipes',
  templateUrl: './my-recipes.component.html',
  styleUrls: ['./my-recipes.component.css'],
})
export class MyRecipesComponent implements OnInit {
  recipes: Recipe[] = [];
  pageEvent!: PageEvent;

  page = 0;
  count = 0;
  pageSize = 3;
  pageSizes: number[] = [3, 6, 9];

  constructor(private recipeService: RecipeService) {}

  ngOnInit(): void {
    this.retrieveRecipes();
  }

  retrieveRecipes(): void {
    this.recipeService
      .getRecipes(this.page, this.pageSize)
      .subscribe((recipes: any) => {
        this.recipes = recipes.content;
        this.count = recipes.totalElements;
      });
  }

  handlePageChange(event: number): void {
    this.page = event;
    this.retrieveRecipes();
  }

  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.count = e.length;
    this.pageSize = e.pageSize;
    this.page = e.pageIndex;
    this.retrieveRecipes();
  }
}
