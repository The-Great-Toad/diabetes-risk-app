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

  /**
   * Retrieves all patients.
   */
  public getPatients(): Observable<Patient[]> {
    console.log('patient service base url', this.patientServiceBaseURL);
    return this.http.get<Patient[]>(this.patientServiceBaseURL);
  }

  /**
   * Retrieves a patient by ID.
   * @param id - The ID of the patient to retrieve.
   */
  public getPatient(id: number): Observable<Patient> {
    return this.http.get<Patient>(`${this.patientServiceBaseURL}/${id}`);
  }

  /**
   * Creates a new patient.
   * @param patient - The patient object to create.
   */
  public createPatient(patient: Patient): Observable<Patient> {
    return this.http.post<Patient>(this.patientServiceBaseURL, patient);
  }

  /**
   * Updates an existing patient.
   * @param patient - The patient object to update.
   */
  public updatePatient(patient: Patient): Observable<Patient> {
    return this.http.put<Patient>(
      `${this.patientServiceBaseURL}/${patient.id}`,
      patient
    );
  }
}
