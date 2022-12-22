import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../constants/api-constants';
import { Category } from '../interfaces/Category';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private categoryUrl = 'category';

  constructor(private http: HttpClient) {}

  getCategoryHints(categoryName: string): Observable<Category[]> {
    let params = new HttpParams().set('name', categoryName);

    return this.http.get<Category[]>(`${API_URL}/${this.categoryUrl}`, {
      params,
    });
  }

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${API_URL}/${this.categoryUrl}/all`);
  }
}
