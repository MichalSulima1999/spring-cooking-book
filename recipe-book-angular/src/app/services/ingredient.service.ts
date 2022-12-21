import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../constants/api-constants';
import { Ingredient } from '../interfaces/Ingredient';

@Injectable({
  providedIn: 'root',
})
export class IngredientService {
  private ingredientUrl = 'ingredient';

  constructor(private http: HttpClient) {}

  getIngredientHints(ingredientName: string): Observable<Ingredient[]> {
    let params = new HttpParams().set('name', ingredientName);

    return this.http.get<Ingredient[]>(`${API_URL}/${this.ingredientUrl}`, {
      params,
    });
  }
}
