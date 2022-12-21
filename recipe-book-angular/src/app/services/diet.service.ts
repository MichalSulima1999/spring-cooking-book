import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../constants/api-constants';
import { Diet } from '../interfaces/diet';

@Injectable({
  providedIn: 'root',
})
export class DietService {
  private dietUrl = 'diet';

  constructor(private http: HttpClient) {}

  getDietHints(dietName: string): Observable<Diet[]> {
    let params = new HttpParams().set('name', dietName);

    return this.http.get<Diet[]>(`${API_URL}/${this.dietUrl}`, {
      params,
    });
  }
}
