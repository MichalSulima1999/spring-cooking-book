import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { RecipeToAdd } from '../interfaces/RecipeToAdd';
import { API_URL } from '../constants/api-constants';
import { RecipeDetails } from '../interfaces/RecipeDetails';
import { RecipeStep } from '../interfaces/RecipeStep';
import { Search } from '../interfaces/Search';
import { RecipePage } from '../interfaces/RecipePage';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root',
})
export class RecipeService {
  private recipeUrl = 'recipe';

  constructor(private http: HttpClient) {}

  getRecipes(page: number, size: number): Observable<RecipePage> {
    let params = new HttpParams().set('pageNum', page).set('pageSize', size);

    return this.http.get<RecipePage>(`${API_URL}/${this.recipeUrl}`, {
      params,
    });
  }

  getRecipe(recipeId: number): Observable<RecipeDetails> {
    return this.http
      .get<RecipeDetails>(`${API_URL}/${this.recipeUrl}/${recipeId}`)
      .pipe(
        map((recipe) => {
          recipe.recipeSteps.sort(
            (a: RecipeStep, b: RecipeStep) => a.stepNumber - b.stepNumber
          );
          return recipe;
        })
      );
  }

  addRecipe(recipe: RecipeToAdd): Observable<RecipeDetails> {
    return this.http.post<RecipeDetails>(
      `${API_URL}/${this.recipeUrl}`,
      recipe,
      httpOptions
    );
  }

  editRecipe(recipe: RecipeToAdd): Observable<RecipeDetails> {
    return this.http.put<RecipeDetails>(
      `${API_URL}/${this.recipeUrl}/${recipe.recipe.id}`,
      recipe,
      httpOptions
    );
  }

  searchRecipes(
    page: number,
    size: number,
    search: Search
  ): Observable<RecipePage> {
    let params = new HttpParams().set('pageNum', page).set('pageSize', size);

    return this.http.post<RecipePage>(
      `${API_URL}/${this.recipeUrl}/search`,
      search,
      {
        headers: httpOptions.headers,
        params: params,
      }
    );
  }
}
