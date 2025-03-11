import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Patient } from '../models/patient';

@Injectable({
  providedIn: 'root',
})
export class PatientService {
  private http = inject(HttpClient);

  public getPatients(): Observable<Patient[]> {
    return this.http.get<Patient[]>('http://localhost:8080/patients');
  }

  public getPatient(id: number): Observable<Patient> {
    return this.http.get<Patient>(`http://localhost:8080/patients/${id}`);
  }

  public updatePatient(patient: Patient): Observable<Patient> {
    return this.http.put<Patient>(`http://localhost:8080/patients`, patient);
  }
}
