import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../constants/api-constants';
import { Step } from '../interfaces/Step';

@Injectable({
  providedIn: 'root',
})
export class StepService {
  private stepUrl = 'step';

  constructor(private http: HttpClient) {}

  getStepHints(stepDescription: string): Observable<Step[]> {
    let params = new HttpParams().set('description', stepDescription);

    return this.http.get<Step[]>(`${API_URL}/${this.stepUrl}`, {
      params,
    });
  }
}
