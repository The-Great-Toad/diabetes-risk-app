import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RiskAssessmentService {
  private http = inject(HttpClient);
  private riskAssessmentServiceBaseURL =
    'http://localhost:8080/risk-assessment';

  public getRiskAssessment(id: number): Observable<string> {
    return this.http.get<string>(`${this.riskAssessmentServiceBaseURL}/${id}`);
  }
}
