import { Component, OnInit } from '@angular/core';
import { Recipe } from 'src/app/interfaces/Recipe';
import { RecipeService } from 'src/app/services/recipe.service';
import { PageEvent } from '@angular/material/paginator';
import { Search } from 'src/app/interfaces/Search';
import { RecipePage } from 'src/app/interfaces/RecipePage';

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
  search: Search = {
    dataOption: 'all',
    orderByField: 'name',
    orderByAscending: true,
  };

  constructor(private recipeService: RecipeService) {}

  ngOnInit(): void {
    this.retrieveRecipes(this.search);
  }

  retrieveRecipes(search: Search): void {
    console.log(search);

    this.search = search;
    this.recipeService
      .searchRecipes(this.page, this.pageSize, search)
      .subscribe((recipePage: RecipePage) => {
        this.recipes = recipePage.content;
        this.count = recipePage.totalElements;
      });
  }

  handlePageChange(event: number): void {
    this.page = event;
    this.retrieveRecipes(this.search);
  }

  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.count = e.length;
    this.pageSize = e.pageSize;
    this.page = e.pageIndex;
    this.retrieveRecipes(this.search);
  }
}
