import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Patient } from '../models/patient';

@Injectable({
  providedIn: 'root',
})
export class PatientService {
  private http = inject(HttpClient);
  private patientServiceBaseURL = 'http://localhost:8080/patients';

  public getPatients(): Observable<Patient[]> {
    return this.http.get<Patient[]>(this.patientServiceBaseURL);
  }

  public getPatient(id: number): Observable<Patient> {
    return this.http.get<Patient>(`${this.patientServiceBaseURL}/${id}`);
  }

  public createPatient(patient: Patient): Observable<Patient> {
    return this.http.post<Patient>(this.patientServiceBaseURL, patient);
  }

  public updatePatient(patient: Patient): Observable<Patient> {
    return this.http.put<Patient>(
      `${this.patientServiceBaseURL}/${patient.id}`,
      patient
    );
  }
}
