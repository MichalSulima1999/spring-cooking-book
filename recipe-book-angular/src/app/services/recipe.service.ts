import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Recipe } from '../interfaces/Recipe';
import { API_URL } from '../constants/api-constants';

@Injectable({
  providedIn: 'root',
})
export class RecipeService {
  private recipeUrl = 'recipe';

  constructor(private http: HttpClient) {}

  getRecipes(page: number, size: number): Observable<any> {
    let params = new HttpParams().set('pageNum', page).set('pageSize', size);

    return this.http.get<any>(`${API_URL}/${this.recipeUrl}`, { params });
  }
}
